package com.capgemini.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateDto {

    private String name;
    private String phone;
    private String email;
    private String oldPassword;
    private String newPassword;
}
