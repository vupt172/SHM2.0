package com.vupt.SHM.dto;

import lombok.Data;

@Data
public class AccountPasswordChangingDto extends AccountDto {
  private String oldPassword;
  private String newPassword;

}
