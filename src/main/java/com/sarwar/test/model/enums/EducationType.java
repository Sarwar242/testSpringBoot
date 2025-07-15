package com.sarwar.test.model.enums;

public enum EducationType {
    SSC("SSC"),
    HSC("HSC"),
    UnderGraduate("UnderGraduate"),
    Graduate("Graduate"),
    PostGraduate("PostGraduate");

    private final String displayName;
    
    EducationType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
