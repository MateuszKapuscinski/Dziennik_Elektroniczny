package com.sda.jz75_security_template.model.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStudentAccountRequest {
    private String username;
    private String password;
    private String confirmPassword;
}
