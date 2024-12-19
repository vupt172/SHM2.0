package com.vupt.SHM.dto;
import com.vupt.SHM.entity.Role;
import lombok.Data;

import java.util.List;

@Data
public class AccountExDto extends AccountDto {
  private List<Role> roles;
}

