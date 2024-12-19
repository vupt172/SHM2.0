package com.vupt.SHM.constant;

public enum DatetimePattern {
    DATE("dd/MM/yyyy"),
    DATETIME("dd/MM/yyyy hh:mm:ss");
    private String pattern;

    DatetimePattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return this.pattern;
    }

    public void setPattern(String pattern) {
    }
}

