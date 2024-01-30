package com.qiqing.entity;

import com.qiqing.parse.JavaMethod;
import net.sf.json.JSON;

import java.util.ArrayList;

public class Classinfo {
    private String name;

    private ArrayList<JSON> annotation;

    private String[] importpackage;

    private ArrayList<javaMethod> methods = new ArrayList<>();



    public String getName() {
        return name;
    }

    public ArrayList<JSON> getAnnotation() {
        return annotation;
    }

    public String[] getImportpackage() {
        return importpackage;
    }

    public void setAnnotation(ArrayList<JSON> annotation) {
        this.annotation = annotation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImportpackage(String[] importpackage) {
        this.importpackage = importpackage;
    }

    public ArrayList<javaMethod> getMethods() {
        return methods;
    }

    public void setMethods(ArrayList<javaMethod> methods) {
        this.methods = methods;
    }

    public void addMethod(javaMethod method)
    {
        this.methods.add(method);
    }
}
