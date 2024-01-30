package com.qiqing.endpoint;

import com.github.javaparser.JavaParser;
import com.github.javaparser.JavaParserBuild;
import com.qiqing.entity.Classinfo;
import com.qiqing.entity.javaMethod;
import com.qiqing.parse.*;
import com.sun.org.apache.bcel.internal.generic.ASTORE;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDConfiguration;
import net.sourceforge.pmd.lang.dfa.pathfinder.CurrentPath;
import net.sourceforge.pmd.lang.dfa.pathfinder.Executable;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import org.checkerframework.checker.units.qual.A;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class ScanStart  extends AbstractJavaRule implements Executable {

    public ArrayList<String> importpackage = new ArrayList<>();

    public static HashMap<String,File> hmf = new HashMap<>();

    public Classinfo  info =  new Classinfo();

    public static ArrayList<Classinfo> infos = new ArrayList<>();

    public static String fdir = "";

    public static String odir = "";

    public ScanStart()
    {
        addRuleChainVisit(ASTPackageDeclaration.class);
        addRuleChainVisit(ASTImportDeclaration.class);
        addRuleChainVisit(ASTClassOrInterfaceDeclaration.class);
        addRuleChainVisit(ASTMethodDeclaration.class);
        //addRuleChainVisit(ASTClassOrInterfaceBody.class);

    }


    @Override
    public Object visit(ASTPackageDeclaration node,Object data)
    {
        if(!importpackage.isEmpty())
        {
            importpackage.clear();
        }

        return data;
    }


    @Override
    public Object visit(ASTImportDeclaration node,Object data)
    {
        if(node.getPackageName() == node.getImportedName())
        {
            importpackage.add(node.getImportedName() + ".*");
        }else {
            importpackage.add(node.getImportedName());
        }
        return data;
    }



    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node,Object data)
    {
        JavaClass jc = new JavaClass(importpackage);
        //JavaMethod jm = new JavaMethod(importpackage.toArray(new String[0]));
        Classinfo info = jc.SpringControllerAST(node);
        ClassDatas.addClassInfo(info);
        //importpackage.clear();
        return data;
    }

    @Override
    public Object visit(ASTMethodDeclaration node,Object data)
    {
        JavaMethod jm = new JavaMethod(importpackage.toArray(new String[]{}),hmf);
        try {
            javaMethod js = jm.JavaMethodAST(node);
            ClassDatas.addMethod(js);
            //System.out.println("method: " + js);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return data;
    }



//    @Override
//    public Object visit(ASTAnnotation node, Object data)
//    {
//
//        System.out.println(importpackage);
//        JavaAnnotation ja = new JavaAnnotation();
//        JSON json = ja.AnnotationAST(node);
//        return data;
//    }

    public static void main(String[] args) throws Exception {
        //String dir = "/Users/qiqing/java-project/javawebDemo/RestTemplate";
        //String dir = "/Users/qiqing/java-project/javawebDemo/jersey-springboot";
        //String dir = "/Users/qiqing/Downloads/ESG-OCS2";
        //String dir = "/Users/qiqing/Downloads/sfrepos_202309_10/EOS-DEO";  // 这个有BUG,ws api
        //String dir = "/Users/qiqing/Downloads/sfrepos_202309_10/ESG-EIMS";
        //String dir = "/Users/qiqing/Downloads/ESG-ACSP";
        startArgs(args);
        //String dir = "/Users/qiqing/Downloads/MCS-CAS";
        String dir = fdir;
        String log = odir + File.separator + "error.log";
        String result = odir + File.separator + "result.txt";
        FileOutputStream fos = new FileOutputStream(log);
        FileOutputStream or = new FileOutputStream(result);
        hmf = JavaFileParse.FileToClasses(new File(dir),fos);
        PMDConfiguration configuration = new PMDConfiguration();
        configuration.setInputPaths(dir);
        configuration.setRuleSets("rules/ServiceRule.xml");
        configuration.setReportFormat("html");
        configuration.setReportFile("pmd.cache");
        configuration.setThreads(0);
        PMD.doPMD(configuration);
        System.out.println("...........");
        URiParse uRiParse = new URiParse(hmf);
        for (Classinfo info : ClassDatas.getInfos())
        {
            uRiParse.ClassParse(info,or,fos);
        }
        System.out.println("Scan finish !!! good lucky :)");
        fos.close();
        or.close();
    }

    public static void startArgs(String[] args) throws IOException
    {
        try {
            InputStream is = Main.class.getResourceAsStream("/banner.txt");
            byte[] b = new byte[is.available()];
            is.read(b);
            System.out.println(new String(b, StandardCharsets.UTF_8));
            fdir = args[0];
            odir = args[1];
        }catch (Exception e)
        {
            System.out.println("\n" +
                    "\n" +
                    "第一个参数为需要扫描的项目目录\n" +
                    "第二个参数为扫描结果的保存目录\n" +
                    "simple: java -jar EndpointTell.jar 项目根目录 扫描结果保存目录");
            System.exit(0);
        }
    }

    @Override
    public void execute(CurrentPath currentPath) {
    }
}
