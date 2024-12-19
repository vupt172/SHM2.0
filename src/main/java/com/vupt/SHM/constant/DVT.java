package com.vupt.SHM.constant;

public enum DVT {
    BO("Bộ"),
    CAI("Cái");
    private String title;

    DVT(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

