package com.qiqing.parse;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.qiqing.entity.MethodParam;
import com.qiqing.entity.javaMethod;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import org.apache.xmlbeans.impl.jam.JamService;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JavaMethod {

    private String[] CanMethodAnnotation  = new String[]{"RequestMapping","GetMapping","PostMapping","PutMapping","Path","GET","POST","PUT"};

    private String[] CanMethodParamAnnotation = new String[]{"RequestBody","RequestParam","RequestPart","PathParam","PathParam","PathParam","PathVariable"};

    private String[] importpakage;

    private HashMap<String,File> allclass;

    private String[] blacklist = new String[]{"org.springframework","jakarta.servlet","javax.servlet","javax.ws"};

    private String[] basicType = new String[]{"String","byte","short","Short","int","Integer","long","float","double","boolean","char","Char","Byte","Float","Double","Boolean"};

    //private String[] basicBType = new String[]{"Byte","Short","Integer","Long","Float","Double","Boolean","Character"};
    //private String[] basicint = new String[]{"int","Integer","short","Short"};
    //private String[] basicchar = new String[]{"char","byte","Char","Byte"};
    //private String[] basicfloat = new String[]{"float","Float"};
    //private String[] basicdouble = new String[]{"double","Double"};
    //private String[] basicboolean = new String[]{"boolean","Boolean"};

    private String[] basictime = new String[]{"java.time","java.util.Date","java.util.Calendar","java.sql.Date","java.sql.Time"};




    /*public JSON ParseType(String typename, String[] allclass, HashMap<String,File> f,int num)
    {
        return null;
    }*/




    // 后期把这个改成配置文件形式!



    private String getExtension(String Name) {
        int dotIndex = Name.lastIndexOf(".");
        if (dotIndex == -1 || dotIndex == Name.length() - 1) {
            return Name;
        } else {
            return Name.substring(dotIndex + 1);
        }
    }

    public JavaMethod(String[] importpack)
    {
        importpakage = importpack;
    }

    public JavaMethod(String[] importpak,HashMap<String,File> allclass)
    {
        importpakage = importpak;
        this.allclass = allclass;
    }

    public ArrayList<ASTAnnotation> MethodAnnotationAST(ASTMethodDeclaration node)
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

    public MethodParam FormalParameterAST(ASTFormalParameter parameter) throws Exception {
        int childnum = parameter.getNumChildren();
        String pack = null;
        JSON anno = null;
        String paramname = null;
        //ArrayList<String> allclasses = new ArrayList<>();


        String[] allclasses = allclass.keySet().toArray(new String[]{});;
        //String[] allclass = allclasses
        ArrayList<ASTFormalParameter> CanParameter = new ArrayList<>();
        MethodParam mp = new MethodParam();
        for (int x  = 0;x<childnum;x++)
        {
            Node node = parameter.getChild(x);
            if(node instanceof ASTType)
            {
                ASTType type = (ASTType) node;
                String paramclassname = type.getTypeImage();
                int num = -1;
                try {
                    num = type.getChild(0).getChild(0).getNumChildren();
                }catch (IndexOutOfBoundsException e)
                {
                    num = type.getChild(0).getNumChildren();
                }

                String typeas = null;
                // 开始解析importpackage
                if(num == 0)
                {
                    typeas = typeAs(paramclassname,allclasses);
                }
                if(num == 1)
                {
                    num = type.getChild(0).getChild(0).getChild(0).getNumChildren();
                    if(num == 1)
                    {
                        try{
                            String s = type.getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getImage();
                            typeas = "List<" + typeAs(s,allclasses) + ">";
                        }catch (Exception e)
                        {
                            typeas = "unknown";
                        }

                    }
                    if(num == 2)
                    {
                        String s1 = type.getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getImage();
                        String s2 = type.getChild(0).getChild(0).getChild(0).getChild(1).getChild(0).getChild(0).getImage();
                        typeas = "Map<" + typeAs(s1,allclasses) + "," + typeAs(s2,allclasses) + ">";
                    }
                }
                pack = typeas;
            }
            if(node instanceof ASTAnnotation)
            {
                JavaAnnotation annotation = new JavaAnnotation();
                anno = annotation.AnnotationAST((ASTAnnotation)node);
            }
            if(node instanceof ASTVariableDeclaratorId)
            {
                ASTVariableDeclaratorId idnode = (ASTVariableDeclaratorId) node;
                paramname = idnode.getImage();
            }
            mp.setAnnotation(anno);
            mp.setName(paramname);
            mp.setClasstype(pack);
        }
        return mp;
    }

    private String typeAs(String paramclassname,String[] allclasses)
    {
        String pack = null;
        ArrayList<String> tmpal = new ArrayList<>();

        if(paramclassname.contains("MultipartFile"))
        {
            pack = "FileUpload";
        }

        for(String s : importpakage)
        {
            String extName = getExtension(s);
            if(extName.equals(paramclassname) && !Arrays.stream(blacklist).anyMatch(str -> s.contains(str)))
            {
                pack = s;
            }
            if(s.contains("*"))
            {
                tmpal.add(s);
            }
        }
        if(pack == null) // .* 情况和基本数据类型
        {

            for(String s : tmpal)
            {
                String tmpx = s.replace("*",paramclassname);
                if(!Arrays.stream(blacklist).anyMatch(str -> s.contains(str)) && Arrays.stream(allclasses).anyMatch(str -> tmpx.equals(str)))
                {
                    pack = tmpx;
                }
            }
        }
        if(pack == null && Arrays.stream(basicType).anyMatch(str->str.contains(paramclassname)))
        {
            pack = paramclassname;
        }

        if(pack == null && isJavaLang(paramclassname))
        {
            pack = "java.lang." + paramclassname;
        }

        return pack;
    }

    private Boolean isJavaLang(String name)
    {
        try{
            Class.forName("java.lang." + name);
            return true;
        }catch (Exception e)
        {
            return false;
        }
    }

//    private Boolean jsJavaUtil(String name)
//    {
//        try{
//            Class.forName("java.util." + name);
//        }
//    }



    public javaMethod JavaMethodAST(ASTMethodDeclaration node) throws Exception {
        javaMethod methodEntity = new javaMethod();
        ArrayList<ASTAnnotation> list = MethodAnnotationAST(node);
        String methodname = node.getName();
        methodEntity.setName(methodname);
        ArrayList<JSON> ans = new ArrayList<>();
        ArrayList<JSON> params = new ArrayList<>();
        for (ASTAnnotation anno : list)
        {
            String anname = anno.getAnnotationName();
            if(Arrays.asList(CanMethodAnnotation).contains(anname))
            {
                JavaAnnotation annotation = new JavaAnnotation();
                JSON json = annotation.AnnotationAST(anno);
                ans.add(json);
            }
        }
        if(ans.isEmpty())
        {
            return methodEntity;
        }
        methodEntity.setAnnotation(ans);
        ASTFormalParameters parameters = node.getFormalParameters();
        Iterator<ASTFormalParameter> ia = parameters.iterator();
        ArrayList<MethodParam> amp = new ArrayList<>();
        while (ia.hasNext())
        {
            ASTFormalParameter af = ia.next();
             amp.add(FormalParameterAST(af));
        }
        methodEntity.setParam(amp);
        return methodEntity;
        //return JSONObject.fromObject(methodEntity);
    }


}
