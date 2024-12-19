package com.vupt.SHM.dto;

import lombok.Data;

@Data
public class AccountDto {
    private long id;
    private String username;
    private String password;
    private String fullName;
    private boolean isSuspended;

}


