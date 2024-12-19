package com.vupt.SHM.entity;

import com.vupt.SHM.entity.AppAuthority;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String code;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<AppAuthority> authorities = new ArrayList<>();

    public void setId(long id) {
        this.id = id;
    }
}
