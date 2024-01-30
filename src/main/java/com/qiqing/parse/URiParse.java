package com.qiqing.parse;


import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.qiqing.entity.*;
import net.sf.ezmorph.MorphException;
import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class URiParse {

    private HashMap<String,File> allClass = new HashMap<>();

    private String[] basicint = new String[]{"int","Integer","short","Short"};

    private String[] basicchar = new String[]{"char","byte","Char","Byte"};

    private String[] basictime = new String[]{"java.time","java.util.Date","java.util.Calendar","java.sql.Date","java.sql.Time"};

    private String[] basicType = new String[]{"string","String","byte","short","Short","int","integer","Integer","Long","long","float","double","boolean","char","Char","Byte","Float","Double","Boolean"};

    private ArrayList<File> af = new ArrayList<>();

    private String upload = "uploadgogogo";

    private String getBody = "GET /param/RequestBody HTTP/1.1\n" +
            "Host: 172.16.119.1:8888\n" +
            "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/116.0\n" +
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
            "Accept-Language: zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2\n" +
            "Accept-Encoding: gzip, deflate\n" +
            "Connection: close\n" +
            "Upgrade-Insecure-Requests: 1\n" +
            "\n";

    private String postBody = "POST /param/RequestBody HTTP/1.1\n" +
            "Host: 172.16.119.1:8888\n" +
            "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/116.0\n" +
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
            "Accept-Language: zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2\n" +
            "Accept-Encoding: gzip, deflate\n" +
            "Connection: close\n" +
            "Upgrade-Insecure-Requests: 1\n" +
            "Content-Type: application/json\n" +
            "Content-Length: 0\n" +
            "\n" +
            "json";


    public URiParse(HashMap<String,File> f)
    {
        allClass = f;
    }

    public void ClassParse(Classinfo info, FileOutputStream fos,FileOutputStream log) throws Exception {


        String path = "";
        String method = "";
        if(!isController(info))
        {
            return;
        }
        System.out.println("正在分析: " + JSONObject.fromObject(info));
        ArrayList<JSON> aj = info.getAnnotation();
        ArrayList<javaMethod> methods = info.getMethods();
        for(JSON so : aj) // 如果是Spring风格
        {
            MorphDynaBean hm =   (MorphDynaBean)JSONSerializer.toJava(so);
            String ano = (String)hm.get("annotation");
            if(ano.contains("Mapping"))
            {
                //String[] ps = (String[])hm.get("path");
                ArrayList<String> ps = (ArrayList<String>) hm.get("path");
                path = ps.get(0);
            }
            if(ano.contains("Path"))
            {
                path = ((String) hm.get("value")).replace("\"","");
            }
//            if(ano.contains("GET") || ano.contains("POST") || ano.contains("PUT"))
//            {
//                method = ano;
//            }
        }

        for(javaMethod md : methods)
        {

            String methodpath = path;
            String methodHM = "";
            aj = md.getAnnotation();
            if(aj == null){
                continue;
            }
            if(!aj.isEmpty())
            {
                for (JSON os : aj)
                {
                    MorphDynaBean hm = (MorphDynaBean) JSONSerializer.toJava(os);
                    String ano = (String)hm.get("annotation");

                    if(ano.contains("Mapping"))
                    {
                       // String[] ps = (String[])hm.get("path");
                        ArrayList<String> ps = (ArrayList<String>)hm.get("path");

                        methodpath = path + "/" + ps.get(0);
                        //String[] mds = (String[]) hm.get("method");
                        ArrayList<String> mds = (ArrayList<String>) hm.get("method");
                        if(mds.size() > 1)
                        {
                            methodHM = mds.get(1);
                        }else {
                            methodHM = mds.get(0);
                        }

                    }
                    if(ano.contains("Path"))
                    {
                        methodpath = path + "/" + (hm.get("value")).toString().replace("\"","");
                    }
                    if(ano.contains("GET") || ano.contains("POST") || ano.contains("PUT"))
                    {
                        methodHM = ano;
                    }
                }
            }
            //System.out.println(path);

            methodpath = methodpath.replace("///","/").replace("//","/").replace("////","/");

            ArrayList<MethodParam> param = md.getParam();

            HashMap<String,Object> hso = new HashMap<>();

            param param1 = new param();

            for(MethodParam mp : param)
            {
                JSON anno = mp.getAnnotation();
                MorphDynaBean ph = (MorphDynaBean) JSONSerializer.toJava(anno);
                String bb = "";
                try{
                    bb = (String)ph.get("annotation");
                }catch (Exception e)
                {
                    bb = "NO";
                }

//                if(ph.get("annotation").equals(null))
//                {
//
//                }else {
//
//                }

                String paramtype = mp.getClasstype();
                HashMap<String,Object> canp = new HashMap<>();
                String name = mp.getName();
                String lv = "";

                if(paramtype == null)
                {
                    af.clear();
                    continue;
                }
                // map只生成basictype
                af.clear();
                if(paramtype.contains("Map<"))
                {

                    try{
                        String[] kv = paramtype.replace("Map","").replace("<","").replace(">","").split(",");
                        HashMap<String,Object> kk = basicType(kv[0],"key");
                        HashMap<String,Object> vv = basicType(kv[1],kk.get("key").toString());
                        canp.put(name,vv);
                    }catch (NullPointerException e)
                    {
                        System.out.println("Map key-value 不是基础数据类型: " + JSONObject.fromObject(info));
                        log.write(("Map key-value 不是基础数据类型: " + JSONObject.fromObject(info) + "\n").getBytes(StandardCharsets.UTF_8));
                        HashMap<String,Object> vv = new HashMap<>();
                        canp.put(name,vv);
                    }

                }
                else if(paramtype.contains("List<"))
                {
                    String oo = paramtype.replace("List","").replace("<","").replace(">","");
                    HashMap<String,Object> hm = basicType(oo,name);
                    if(hm.isEmpty())
                    {
                        String[] importpack = info.getImportpackage();
                        ArrayList<String> airimport = new ArrayList<>(Arrays.asList(importpack));
                        hm = complexType(oo,allClass,airimport,name,null);
                    }
                    ArrayList<Object> d = new ArrayList<>();
                    d.add(hm.get(name));
                    canp.put(name,d);
                }

                else if(Arrays.stream(basicType).anyMatch(s -> s.contains(getExtension(paramtype))) )
                {
                    canp = basicType(paramtype,name);
                    lv = "0";
                }else {
                    String[] importpack = info.getImportpackage();
                    ArrayList<String> airimport = new ArrayList<>(Arrays.asList(importpack));
                    canp = complexType(paramtype,allClass,airimport,name,null);
                    lv = "1";
                }
                Object t = canp.get(name);
                param1.addType(name,t);
                param1.addAnno(name,bb);
                param1.addLevel(name,lv);
                if(bb == "NO")
                {
                    param1.addAnp("NO","NO");
                }else {
                    try{
                        param1.addAnp(bb,ph.get("name").toString());
                    }catch (MorphException e)
                    {
                        param1.addAnp(bb,null);
                    }
                }


            }
            HashMap<String, String> parseAnno = param1.getAnno();
            HashMap<String, Object> parseType = param1.getType();
            HashMap<String, String> level = param1.getLevel();
            HashMap<String, String> parseANP = param1.getAnp();
            Set<String> as = parseAnno.keySet();

            String body = "";
            String query = "";
            for(String s : as)
            {
                if(parseAnno.get(s).contains("RequestBody")) //代表是POST
                {

                    body = "";
                    String lvbody = level.get(s);
                    if(lvbody != "0")
                    {
                        try{
                            body = JSONObject.fromObject(parseType.get(s)).toString();
                        }catch (Exception e)
                        {
                            try {
                                body = JSONArray.fromObject(parseType.get(s)).toString();
                            }catch (Exception ex)
                            {
                                body = JSONArray.fromObject(parseType).toString();
                            }
                        }
                    }else {
                        body = body + s + "=" + parseType.get(s).toString();
                    }


                    Set<String> s1 = parseType.keySet();
                    for(String sz : s1)
                    {
                        String lv = level.get(sz);
                        if(sz == s) // 不想太多了
                        {
                            continue;
                        }
                        if(lv == "0" && lvbody == "1")
                        {
                            query = query + "&" + sz + "=" + parseType.get(sz);
                        }else if(lv == "0" & lvbody == "0")
                        {
                            body = body + "&" + sz + "=" + parseType.get(sz);
                        }
                        else { // 代表在RequestBody的情况下,还有结构体存在!!,暂时不考虑这种,因为他有可能是model

                        }

                    }
                }
                //  暂时不处理有关 @RequestParam User user 相关的请求!
                else if(parseAnno.get(s).contains("RequestParam"))
                {
                    body = "";
                    query = "";
                    String lv = level.get(s);
                    Object dd = parseType.get(s);
                    String pm = parseANP.get("RequestParam");
                    if(pm == "")
                    {
                        pm = s;
                    }
                    if(methodHM.toLowerCase().contains("get"))
                    {
                        if(lv == "0")
                        {
                            query = query + "&" + pm + "=" + parseType.get(s);
                        }else {
                            //parseType.get(s);
                            System.out.println("RequestParam User情况出现了! " + JSONObject.fromObject(md));
                        }
                    }
                    if(methodHM.toLowerCase().contains("post"))
                    {

                        if(lv == "0")
                        {
                            body = body + "&" + pm + "=" + parseType.get(s);
                        }else if(dd != null && (dd.toString().contains("FileUpload") || s.contains("multipartFile")))
                        {
                            body = upload + "=" + pm;
                        }else {
                            Object o = parseType.get(s);
                            if(o instanceof Map)
                            {
                                HashMap<String,Object> oh = (HashMap<String,Object>) o;
                                if(oh.isEmpty())
                                {
                                    body = body + "&" + "mapks" + "=" + "mapkv";
                                }
                                for(String ss : oh.keySet())
                                {
                                    body = body + "&" + ss + "=" + oh.get(ss);
                                }
                            }if(o instanceof List)
                            {
                                ArrayList<String> ah = (ArrayList<String>) o;

                                for(Object hs : ah)
                                {
                                    body = body + "&" + pm + "=" + hs;
                                }
                            }
                        }
                    }


                    Set<String> s1 = parseType.keySet();
                    for(String sz : s1)
                    {
                        lv = level.get(sz);
                        if(sz == s) // 不想太多了
                        {
                            continue;
                        }
                        if(lv == "0" && methodHM.toLowerCase().contains("get") )
                        {
                            query = query + "&" + sz + "=" + parseType.get(sz);
                        }else if(lv == "0" && methodHM.toLowerCase().contains("post"))
                        {
                            body = body + "&" + sz + "=" + parseType.get(sz);
                        }
                        else { // 代表在RequestBody的情况下,还有结构体存在!!,暂时不考虑这种,因为他有可能是model
                        }
                    }

                }
                else if(parseAnno.get(s).contains("RequestPart"))
                {
                    body = upload + "=" + s;
                }
                else if(parseAnno.get(s).contains("FormParam"))
                {
                    Object o = parseType.get(s);
                    //String pm = parseANP.get("FormParam");
                    if(o instanceof Map)
                    {
                        HashMap<String,Object> oh = (HashMap<String,Object>) o;
                        if(oh.isEmpty())
                        {
                            body = body + "&" + "mapks" + "=" + "mapkv";
                        }
                        for(String ss : oh.keySet())
                        {
                            body = body + "&" + ss + "=" + oh.get(ss);
                        }
                    }if(o instanceof List)
                    {
                        ArrayList<String> ah = (ArrayList<String>) o;
                        for(String hs : ah)
                        {
                            body = body + "&" + "NONONO" + "=" + hs;
                        }
                    }
                    // body = body + "&" + s + "=" + parseType.get(s);
                }
                else if(parseAnno.get(s).contains("QueryParam"))
                {
                    query = query + "&" + s + "=" + parseType.get(s);
                }
                else if(parseAnno.get(s).contains("PathParam") || parseAnno.get(s).contains("PathVariable"))
                {
                    Object name = parseType.get(s);
                    assert path != null;
                    if(name == null)
                    {
                        name = "1";
                    }
                    methodpath = methodpath.replace("{"+s+"}",name.toString());
                } else {
                    if(methodHM.toLowerCase().contains("get"))
                    {
                        String lv = level.get(s);
                        if(lv == "0")
                        {
                            query = query + "&" + s + "=" + parseType.get(s);
                        }else { // lv 不等于0的情况,记录
                        }
                    } else {
                        String lv = level.get(s);
                        if(lv == "0")
                        {
                            for(String sb :parseType.keySet())
                            {
                                body = body + "&" + sb + "=" + parseType.get(sb);
                            }
                        }else {
                            try{
                                body = JSONObject.fromObject(parseType.get(s)).toString();
                            }catch (Exception e)
                            {
                                try {
                                    body = JSONArray.fromObject(parseType.get(s)).toString();
                                }catch (Exception ex)
                                {
                                    System.out.println("JSON body error: " + JSONObject.fromObject(info));
                                }

                            }
                        }
                    }
                }
            }
            if(body.indexOf("&") == 0)
            {
                body = body.substring(1);
            }
            if(query.indexOf("&") == 0)
            {
                query = "?" + query.substring(1);
            }
            if(methodpath.contains("/**") || methodpath.contains("/*"))
            {
                methodpath = methodpath.replace("/**","/1").replace("/*","/1");
            }
            //path = methodpath;
            if(methodpath.indexOf("/") != 0)
            {
                methodpath = "/" + methodpath;
            }
            //System.out.println("-----------Body---------------");
            fos.write("-----------Body---------------\n".getBytes(StandardCharsets.UTF_8));
            //System.out.println("method: " + methodHM);
            fos.write(("method: " + methodHM + "\n").getBytes(StandardCharsets.UTF_8));
            //System.out.println("path: " + methodpath);
            fos.write(("path: " + methodpath + "\n").getBytes(StandardCharsets.UTF_8));
            //System.out.println("body: " + body);
            fos.write(("body: " + body + "\n").getBytes(StandardCharsets.UTF_8));
            //System.out.println("query: " + query);
            fos.write(("query: " + query + "\n").getBytes(StandardCharsets.UTF_8));
        }
    }





    private HashMap<String,Object> basicType(String name,String typename)
    {

        HashMap<String,Object> hm = new HashMap<>();
        if(typename.equals("serialVersionUID"))
        {

            return hm;
        }

        //HashMap<String,Object> hm = new HashMap<>();
        Object zz = null;
        if(name.contains("Map"))
        {
            zz = new HashMap<>();
        }
        if(name.contains("List") || name.contains("[]"))
        {
            zz = new String[]{};
        }
        if(name.toLowerCase().contains("string"))
        {
            zz = "S1";
        }
        if(name.toLowerCase().contains("long"))
        {
            zz =  1000000000;
        }

        if(Arrays.stream(basicint).anyMatch(str -> str.contains(name)))
        {
            zz = 1;
        }
        if(Arrays.stream(basicchar).anyMatch(str -> str.contains(name)))
        {
            zz = 41;
        }
        if(name.toLowerCase().contains("float"))
        {
            zz = 1.1;
        }
        if(name.toLowerCase().contains("double"))
        {
            zz = 1.01;
        }
        if(name.toLowerCase().contains("boolean"))
        {
            zz = true;
        }
        if(Arrays.stream(basictime).anyMatch(str -> str.contains(name)))
        {
            Date currentTime = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
            zz = format.format(currentTime);
        }
        if(zz != null)
        {
            hm.put(typename,zz);
        }
        return hm;
    }

    private HashMap<String,Object> complexType(String name, HashMap<String, File> allclass, ArrayList<String> ayimport, String typename,String classname) throws Exception
    {
        Set<String> key = allclass.keySet();
        HashMap<String,Object> hm = new HashMap<>();
        Boolean bl = true;
        int x = 0;
        if(name.equals("FileUpload"))
        {
            hm.put(typename,"FileUpload");
            return hm;
            //bl = false;
        }
        for(String s : ayimport)
        {
            if(s.contains(".*"))
            {
                if(name.contains(s.replace("*",""))) {
                    File rf = allclass.get(name);
                    if(rf == null)
                    {
                        bl = false;
                    }else {
                        hm.put(typename, ParseField(rf, allclass));
                        return hm;
                    }

                   // bl = false;
                }
            }
            if(s.contains(name))
            {
                if(s.contains("java.math"))
                {
                    hm.put(typename,1.01);
                    return hm;
                    //bl = false;
                }
                else if(s.contains("JSONObject"))
                {
                    hm.put(typename,new HashMap<>());
                    return hm;
                    //bl = false;
                }

//                else if(s.contains("java.io.InputStream"))
//                {
//                    ArrayList<String> id = new ArrayList<>();
//                    id.add("inputstream");
//                    hm.put(typename,id);
//                    bl = false;
//                }
                else {
                    File rf = allclass.get(s);
                    if(rf == null)
                    {
                        //hm.put(typename,ParseField(rf,allclass));
                        bl = false;
                    }else {
                        hm.put(typename,ParseField(rf,allclass));
                        return hm;
                        //bl = false;
                    }

                }

            }
        }
        if(bl)
        {
            if(key.contains(classname + "." + name))
            {
                File rf = allclass.get(classname + "." + name);
                hm.put(typename,ParseField(rf,allclass));
            }
        }

        return hm;
    }


    private String getExtension(String Name) {
        int dotIndex = Name.lastIndexOf(".");
        if (dotIndex == -1 || dotIndex == Name.length() - 1) {
            return Name;
        } else {
            return Name.substring(dotIndex + 1);
        }
    }


    private ArrayList<String> ParseImports(File f) throws FileNotFoundException {
        JavaParser jp = new JavaParser();
        ParseResult<CompilationUnit> pr =  jp.parse(f);
        ArrayList<String> ayimport = new ArrayList<>();
        NodeList<ImportDeclaration> imports = pr.getResult().get().getImports();
        for (ImportDeclaration ip : imports)
        {
            String packagename = null;
            int col = ip.getRange().get().end.column;
            int col1 = ip.getChildNodes().get(0).getRange().get().end.column;
            if(col == col1 + 3)
            {
                packagename = ip.getNameAsString() + ".*";
            }else {
                packagename = ip.getNameAsString();
            }
            ayimport.add(packagename);
        }
        return ayimport;
    }


    public HashMap<String,Object> ParseField(File f, HashMap<String,File> allclass) throws Exception
    {

        JavaParser jp = new JavaParser();
        if(af.contains(f))
        {
            HashMap<String,Object> o = new HashMap<>();
            o.put("Huihuan","");
            return o;
        }
        af.add(f);
        ParseResult<CompilationUnit> pr = null;
        try {
             pr =  jp.parse(f);
        }catch (Throwable e)
        {
            System.out.println(f);
            System.exit(0);
        }

        NodeList<BodyDeclaration<?>> nodes =  pr.getResult().get().getPrimaryType().get().getMembers();
        String mypack = pr.getResult().get().getPackageDeclaration().get().getName().toString();
        ArrayList<String> ayimport = new ArrayList<>();
        HashMap<String,Object> hm = new HashMap<>();
        NodeList<ImportDeclaration> imports = pr.getResult().get().getImports();
        for (ImportDeclaration ip : imports)
        {
            String packagename = null;
            int col = ip.getRange().get().end.column;
            int col1 = ip.getChildNodes().get(0).getRange().get().end.column;
            if(col == col1 + 3)
            {
                packagename = ip.getNameAsString() + ".*";
            }else {
                packagename = ip.getNameAsString();
            }
            ayimport.add(packagename);
        }
        for(int x =0; x<nodes.size();x++)
        {
            HashMap<String,Object> hs = new HashMap<>();
            if(nodes.get(x) instanceof FieldDeclaration)
            {
                FieldDeclaration fd = (FieldDeclaration) nodes.get(x);
                String name = fd.getVariables().get(0).getName().asString();
                String type = fd.getElementType().toString();
                // 基础类 严格限定最后一个类
                String basictype = getExtension(type);
                //String zz = basictype;


                if(basictype.contains("List<"))
                {
                    HashMap<String,Object> hl = new HashMap<>();
                    String oo = basictype.replace("List","").replace("<","").replace(">","");
                    hl = basicType(oo,name);
                    if(hl.isEmpty())
                    {
                        hl = complexType(oo,allClass,ayimport,name,mypack);
                    }
                    ArrayList<Object> d = new ArrayList<>();
                    d.add(hl.get(name));
                    hs.put(name,d);
                }else {
                    hs = basicType(type,name);
                }
                if(hs.isEmpty())
                {
                    hs = complexType(type,allclass,ayimport,name,mypack);
                }
                // 自定义类,如果该类不在可解析类中,则直接忽略
                /*ArrayList<String> key = (ArrayList<String>) allclass.keySet();
                for(String s : ayimport)
                {
                    if(s.contains(".*"))
                    {
                        type = s.replace("*",type);
                        if(key.contains(type)) {
                            File rf = allclass.get(type);
                            hm.put(name, ParseField(rf, allclass));
                        }
                    }
                    else if(s.contains(type))
                    {
                        if(s.contains("java.math"))
                        {
                            hm.put(name,"1.01");
                        }
                        File rf = allclass.get(s);
                        hm.put(name,ParseField(rf,allclass));
                    }
                }*/

                // 如果不是基础类就循环
//                for(int num = 0;num<key.size();num ++)
//                {
//                    if(key.get(num).contains(type))
//                    {
//                        File rf = allclass.get(key.get(num));
//                        hm.put(name,ParseField(rf,allclass));
//                        break;
//                    }
//                }

                /*
                if(Arrays.stream(allclass).anyMatch(str -> str.contains(type)))
                {

                }

                 */
            }
            for(String s : hs.keySet())
            {
                hm.put(s,hs.get(s));
            }

        }
        return hm;
    }




    public boolean isController(Classinfo info)
    {
        boolean result = false;
        ArrayList<javaMethod> ajm =  info.getMethods();
        ArrayList<JSON> aj = info.getAnnotation();
        if(!aj.isEmpty())
        {
            result = true;
        }
        for(javaMethod jm : ajm)
        {
            ArrayList<JSON> jx = jm.getAnnotation();
            if(jx != null && !jx.isEmpty() && !result)
            {
                result = true;
            }
        }
        return result;
    }

    public class param
    {
        private HashMap<String,String> anno = new HashMap<>();

        private HashMap<String,Object> type = new HashMap<>();

        private HashMap<String,String> level = new HashMap<>();

        private HashMap<String,String> anp = new HashMap<>();

        public HashMap<String, Object> getType() {
            return type;
        }

        public HashMap<String, String> getAnno() {
            return anno;
        }

        public void addAnno(String name,String annotation)
        {
            anno.put(name,annotation);
        }
        public void addType(String name,Object jtype)
        {
            type.put(name,jtype);
        }

        public HashMap<String, String> getLevel() {
            return level;
        }
        public void addLevel(String name,String le)
        {
            level.put(name,le);
        }

        public HashMap<String, String> getAnp() {
            return anp;
        }

        public void addAnp(String ann,String value) {
            anp.put(ann,value);
        }
    }




}
