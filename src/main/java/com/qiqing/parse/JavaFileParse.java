package com.qiqing.parse;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.comments.CommentsCollection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class JavaFileParse {

    public static HashMap<String,File> FileToClasses(String src) throws IOException {
        return FileToClasses(new File(src),null);
    }

    public static HashMap<String,File> FileToClasses(File  dir, FileOutputStream fos) throws IOException {
        ArrayList<File> af = new ArrayList<>();
        HashMap<String,File> classes = new HashMap<>();
        traverseFiles(dir,af);
        JavaParser jp = new JavaParser();
        for (File fs : af)
        {
            try{
                ParseResult<CompilationUnit> pr =  jp.parse(fs);
                CompilationUnit compilationUnit = pr.getResult().get();
                String classname = "";
                classname = compilationUnit.getPackageDeclaration().get().getName() + "." + compilationUnit.getPrimaryTypeName().get().toString();
                classes.put(classname,fs);
            }catch (Exception e)
            {
                fos.write(("这个类文件可能是个废弃的注释文件: " + fs.getAbsolutePath() + "\n").getBytes(StandardCharsets.UTF_8));
                System.out.println("这个类文件可能是个废弃的注释文件: " + fs.getAbsolutePath());
            }
        }
        return classes;
    }

    public static void traverseFiles(File directory, ArrayList<File> af) {
        // 检查目录是否存在
        if (!directory.exists()) {
            System.out.println("目录不存在");
            return;
        }

        // 获取目录下的所有子文件和子目录
        File[] files = directory.listFiles();

        // 遍历子文件和子目录
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".java") && !file.getAbsolutePath().contains("src/test")) {
                // 处理文件
                af.add(file);
            } else if (file.isDirectory()) {
                // 递归遍历子目录
                traverseFiles(file,af);
            }
        }
    }


    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<File> files = new ArrayList<>();
        File f = new File("/Users/qiqing/java-project/javawebDemo/RestTemplate");
        traverseFiles(f,files);
        for(File fs : files)
        {
            //FileToClasses(fs);
        }
    }

}
