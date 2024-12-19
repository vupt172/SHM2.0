package com.vupt.SHM.entity;

import com.vupt.SHM.constant.EquipmentStatus;
import com.vupt.SHM.entity.Department;
import com.vupt.SHM.entity.EquipmentHistory;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Equipment extends BaseEntity<String> {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String code;
    private int year = -1;
    private int count = 1;
    private long price = -1L;
    @Column(nullable = false)
    private EquipmentStatus status;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    private String owner;
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    private String note;
    @OneToMany(mappedBy = "equipment", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<EquipmentHistory> equipmentHistoryList;
    private boolean isDeleted;

    public String generateCode() {
        return this.category.getCode() + getId();
    }
}

