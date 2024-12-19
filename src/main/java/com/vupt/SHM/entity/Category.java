package com.vupt.SHM.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
public class Category extends BaseEntity<String> {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    String code;
    private boolean isSuspended;

    public Category() {
    }

    public Category(String name, String code) {
        this.name = name;
        this.code = code;
    }
}


