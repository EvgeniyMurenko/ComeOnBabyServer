package com.ComeOnBaby.enums;

public enum MainPathEnum {

    mainPath("opt/tomcat/webapps/comeonbaby/"),
    mainWebPath("http://34.253.54.136:8080/");
/*    mainWebPath("http://localhost:8080/");*/

    private String value;

    MainPathEnum(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
