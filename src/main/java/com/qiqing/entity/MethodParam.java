package com.qiqing.entity;

import net.sf.json.JSON;

public class MethodParam {
    private JSON annotation;

    private String classtype;

    private String name;

    public void setAnnotation(JSON annotation) {
        this.annotation = annotation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClasstype(String classtype) {
        this.classtype = classtype;
    }

    public String getName() {
        return name;
    }

    public JSON getAnnotation() {
        return annotation;
    }

    public String getClasstype() {
        return classtype;
    }
}
