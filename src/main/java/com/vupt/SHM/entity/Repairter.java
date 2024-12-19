package com.vupt.SHM.entity;


import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class Repairter extends BaseEntity<String> {
    private String name;
    private String company;
    private String phone;
    private String note;

}


