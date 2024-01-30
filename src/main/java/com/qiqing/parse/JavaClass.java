package com.qiqing.parse;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.qiqing.entity.Classinfo;
import com.qiqing.entity.Mapping;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sourceforge.pmd.lang.java.ast.ASTAnnotation;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class JavaClass {

    private String[] Controllersign = new String[]{"Controller","RestController","Path",/*"WebService"*/};

    private String[] needController = new String[]{"PutMapping","PostMapping","GetMapping","RequestMapping","Path","GET","POST","PUT"};

    private Classinfo info;
    public JavaClass(ArrayList<String> importpack)
    {
        info = new Classinfo();
        //System.out.println(importpack);
        String[] array = importpack.toArray(new String[importpack.size()]);
        info.setImportpackage(array);
    }

    public ArrayList<ASTAnnotation> AnnoList(ASTClassOrInterfaceDeclaration node)
    {
        Iterator iterator = node.getParent().children().iterator();
        ArrayList<ASTAnnotation> result = new ArrayList<>();
        while (iterator.hasNext())
        {
            Object o1 = iterator.next();
            if(o1.getClass().isAssignableFrom(ASTAnnotation.class))
            {
                result.add((ASTAnnotation) o1);
            }
        }
        return result;
    }



    public Classinfo SpringControllerAST(ASTClassOrInterfaceDeclaration node)
    {
        String classname = node.getSimpleName();
        ArrayList<ASTAnnotation> list = AnnoList(node);
        ArrayList<JSON> ans = new ArrayList<>();

        for (ASTAnnotation anno : list)
        {
            String name = anno.getAnnotationName();
            //System.out.println(name);
            if(Arrays.asList(Controllersign).contains(name))
            {
                info.setName(classname);
            }
            if(Arrays.asList(needController).contains(name))
            {
                JavaAnnotation annotation = new JavaAnnotation();
                JSON json = annotation.AnnotationAST(anno);
                ans.add(json);
            }
        }
        info.setAnnotation(ans);
        if(info.getName() != null)
        {
            if(info.getAnnotation().isEmpty())
            {
                Mapping mapping = new Mapping("RequestMapping","/");
                mapping.setMethod(new String[]{"GET","POST"});
                ArrayList<JSON> ajs = new ArrayList<>();
                JSON json = JSONObject.fromObject(mapping);
                ajs.add(json);
                info.setAnnotation(ajs);
            }
        }
        return info;
    }





    public static void main(String[] args) throws Exception {
        String  javacode = "public class Test {" +
                "" +
                "}";
        JavaParser parse = new JavaParser();
        ParseResult<CompilationUnit> result =  parse.parse(javacode);


    }

}
