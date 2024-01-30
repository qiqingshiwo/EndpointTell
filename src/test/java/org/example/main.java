package org.example;
import com.alibaba.fastjson2.JSONObject;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.base.Strings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import com.qiqing.entity.Base;
import com.qiqing.entity.Mapping;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import net.sf.json.util.JSONBuilder;
import net.sf.ezmorph.bean.MorphDynaBean;

public class main {


    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException {
        Mapping mapping  = new Mapping("aaaMapping","/dcdcds");
        mapping.getClass().getDeclaredField("path");
        JSON js = net.sf.json.JSONObject.fromObject(mapping);
        String json = JSONObject.toJSONString(mapping);
        System.out.println(mapping);
        System.out.println(json);

        System.out.println(js);
        ArrayList<String> als = new ArrayList<>();
        als.add("okok");
        MorphDynaBean obj =  (MorphDynaBean)JSONSerializer.toJava(js);obj.set("method",als);
        System.out.println(
                obj
        );
    }
}
