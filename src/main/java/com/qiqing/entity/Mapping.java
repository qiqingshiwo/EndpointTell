package com.qiqing.entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class Mapping extends Base {
    private String Annotation;
    private String name;

    private String[] value = new String[]{};

    private String[] path = new String[]{};

    private String[] params = new String[]{};

    private String[] headers = new String[]{};

    private String[] consumes = new String[]{};

    private String[] produces = new String[]{};

    private String[] method = new String[]{};

    public Mapping(String Anno,String name)
    {
        Annotation = Anno;
        value = this.path = new String[]{name};
    }

    public Mapping(String Anno)
    {
        Annotation = Anno;
    }

    public String getAnnotation()
    {
        return Annotation;
    }

    public String[] getMethod() {
        return method;
    }

    public void setMethod(String[] method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public String[] getConsumes() {
        return consumes;
    }

    public String[] getHeaders() {
        return headers;
    }

    public String[] getParams() {
        return params;
    }

    public String[] getPath() {
        return path;
    }

    public String[] getProduces() {
        return produces;
    }

    public String[] getValue() {
        return value;
    }

    public void setPath(String[] path) {
        this.path = path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAnnotation(String annotation) {
        Annotation = annotation;
    }

    public void setConsumes(String[] consumes) {
        this.consumes = consumes;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public void setProduces(String[] produces) {
        this.produces = produces;
    }

    public void setValue(String[] value) {
        this.value = value;
    }
}
