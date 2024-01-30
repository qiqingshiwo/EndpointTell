package com.qiqing.entity;

public class PathVariable extends Base {

    private String Annotation = "PathVariable";
    private String name;

    private String value;

    private Boolean required = true;

    public PathVariable(String name)
    {
        this.name = this.value = name;
    }

    public PathVariable()
    {

    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getAnnotation() {
        return Annotation;
    }

    public Boolean getRequired() {
        return required;
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

    public void setRequired(Boolean required) {
        this.required = required;
    }
}

