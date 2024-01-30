package com.qiqing.entity;

public class WsRsParam extends Base{
    private String Annotation = "";
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

    public WsRsParam(String name)
    {
        Annotation = name;
    }
    public WsRsParam(String name,String value)
    {
        Annotation = name;
        this.value = value;
    }

}
