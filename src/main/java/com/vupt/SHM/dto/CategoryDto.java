package com.vupt.SHM.dto;

import lombok.Data;

@Data
public class CategoryDto {
    private long id;
    private String name;
    private String code;
    private boolean isSuspended;

    @Override
    public String toString() {
        return name;
    }
}
