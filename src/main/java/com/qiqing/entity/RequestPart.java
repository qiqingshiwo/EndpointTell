package com.qiqing.entity;

public class RequestPart extends Base {

    private String Annotation = "RequestPart";
    private String name ;
    private String value;
    private Boolean required = true;

    public RequestPart(String name)
    {
        this.name = this.value = name;
    }

    public String getAnnotation() {
        return Annotation;
    }

    public Boolean getRequired() {
        return required;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public void setAnnotation(String annotation) {
        Annotation = annotation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public RequestPart()
    {

    }


}
