package com.qiqing.entity;

import net.sf.json.JSON;

import java.util.ArrayList;

public class javaMethod {
    private String name;
    private ArrayList<JSON> annotation;
    private ArrayList<MethodParam> param;

    public ArrayList<JSON> getAnnotation() {
        return annotation;
    }

    public String getName() {
        return name;
    }

    public ArrayList<MethodParam> getParam() {
        return param;
    }

    public void setAnnotation(ArrayList<JSON> annotation) {
        this.annotation = annotation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParam(ArrayList<MethodParam> param) {
        this.param = param;
    }
}
