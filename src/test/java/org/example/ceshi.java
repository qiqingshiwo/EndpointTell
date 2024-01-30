package org.example;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.comments.CommentsCollection;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;

import java.io.File;
import java.io.FileNotFoundException;

public class ceshi {
    public static void main(String[] args) throws FileNotFoundException {
        JavaParser jp = new JavaParser();
        ParseResult<CompilationUnit> pr =  jp.parse(new File("/Users/qiqing/java-project/javawebDemo/RestTemplate/src/main/java/com/example/resttemplate/Controller/ParamController.java"));
        CommentsCollection ct = pr.getCommentsCollection().get();
        NodeList<BodyDeclaration<?>>  nodes =  pr.getResult().get().getPrimaryType().get().getMembers();
        int xx = nodes.size();
        //int col1 = pr.getResult().get().getImports().get(1).getRange().get().end.column;
        //int col2 = pr.getResult().get().getImports();
        NodeList<ImportDeclaration> imports = pr.getResult().get().getImports();
        for (ImportDeclaration ip : imports)
        {
            int col = ip.getRange().get().end.column;
            System.out.println(col);
            int col1 = ip.getChildNodes().get(0).getRange().get().end.column;
            System.out.println(col1);
            if(col == col1 + 3)
            {
                System.out.println(ip.getNameAsString() + ".*");
            }else {
                System.out.println(ip.getNameAsString());
            }
        }


        for(int x =0; x<xx;x++)
        {
            if(nodes.get(x) instanceof FieldDeclaration)
            {
                FieldDeclaration fd = (FieldDeclaration) nodes.get(x);
                String name = fd.getVariables().get(0).getName().asString();
                String type = fd.getElementType().toString();
                System.out.println(name);
                System.out.println(type);
            }
        }

        CompilationUnit compilationUnit = pr.getResult().get();
        compilationUnit.getPackageDeclaration();
        compilationUnit.getPrimaryTypeName();
        System.out.println(ct);
        System.out.println(pr);
        System.out.println(compilationUnit.getPackageDeclaration().get().getName() + "." + compilationUnit.getPrimaryTypeName().get().toString());
    }
}
