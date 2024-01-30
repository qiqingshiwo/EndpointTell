package com.qiqing.entity;

public class WsRsHttpMethod extends Base{
    private String Annotation = "";

    public String getAnnotation() {
        return Annotation;
    }

    public void setAnnotation(String annotation) {
        Annotation = annotation;
    }

    public WsRsHttpMethod(String name)
    {
        Annotation = name;
    }
    public WsRsHttpMethod()
    {

    }
}
