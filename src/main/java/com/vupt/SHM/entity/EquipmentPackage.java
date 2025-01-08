package com.vupt.SHM.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;
@Data
@Entity
public class EquipmentPackage extends BaseEntity<String> {
    @Column(nullable = false,unique = true)
    private String name;
    private String detail;
    private long price;
    private Date date;
    private String equipmentCodeList;

    @Override
    public String toString() {
        return name;
    }
}
