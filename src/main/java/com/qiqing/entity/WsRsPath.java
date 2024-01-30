package com.qiqing.entity;

public class WsRsPath extends Base{
    private String Annotation = "Path";
    private String value;

    public String getAnnotation() {
        return Annotation;
    }

    public String getValue() {
        return value;
    }

    public void setAnnotation(String annotation) {
        Annotation = annotation;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public WsRsPath(String name)
    {
        value = name;
    }

    public WsRsPath()
    {
    }
}
