package com.vupt.SHM.constant;


public enum DepartmentType {
    PHONG("Phòng"),
    TO("Tổ"),
    KHOA("Khoa"),
    PHONG_KHAM("Phòng Khám"),
    KHO("Kho"),
    KHU("Khu"),
    KHAC("Khác");

    private String title;


    DepartmentType(String title) {
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}

