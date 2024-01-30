package com.qiqing.entity;

public class RequestBody extends Base {

    private String Annotation = "RequestBody";
    private Boolean required = true;

    public RequestBody()
    {
    }

    public RequestBody(Boolean required)
    {
        this.required = required;
    }

    public Boolean getRequired() {
        return required;
    }

    public String getAnnotation() {
        return Annotation;
    }

    public void setAnnotation(String annotation) {
        Annotation = annotation;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }
}
