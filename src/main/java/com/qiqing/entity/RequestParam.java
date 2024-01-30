package com.qiqing.entity;

public class RequestParam extends Base {

    private String Annotation = "RequestParam";
    private String name;

    private String value;

    private Boolean required = true;

    private String defaultValue;

    public RequestParam(String name,String value,Boolean required,String defaultValue)
    {
        name= name;
        value=value;
        required=required;
        defaultValue=defaultValue;
    }

    public RequestParam()
    {

    }

    public RequestParam(String name)
    {
        this.name = value = name;
    }

    public Boolean getRequired() {
        return required;
    }

    public String getAnnotation() {
        return Annotation;
    }

    public String getName() {
        return name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getValue() {
        return value;
    }

    public void setAnnotation(String annotation) {
        Annotation = annotation;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public void setName(String name) {
        this.name = this.value = name;
    }

    public void setValue(String value) {
        this.value = this.name = value;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

}
