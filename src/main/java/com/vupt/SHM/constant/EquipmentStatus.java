package com.vupt.SHM.constant;

public enum EquipmentStatus {
    NEW("Mới"),
    USED("Đang sử dụng"),
    DESTROYED("Bị hư"),
    STORAGE("Lưu trữ");

    EquipmentStatus(String title) {
        this.title = title;
    }

    private String title;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

