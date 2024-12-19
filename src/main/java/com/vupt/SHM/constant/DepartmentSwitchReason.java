package com.vupt.SHM.constant;

public enum DepartmentSwitchReason {
    BAN_GIAO("BÀN GIAO"),
    THU_HOI("THU HỒI"),
    KHAC("KHÁC");
    private String reason;

    DepartmentSwitchReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}


