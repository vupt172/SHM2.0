package com.vupt.SHM.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class AppAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(unique = true, nullable = false)
    private String code;
    @ManyToOne
    @JoinColumn(name = "app_function_id")
    private AppFunction appFunction;


    public AppAuthority() {
    }

    public AppAuthority(String name, String code) {
        this.name = name;
        this.code = code;
    }

}


