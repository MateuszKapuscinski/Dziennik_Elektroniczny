package com.sda.jz75_security_template.model.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {
    private String username;
    private String password;
    private String confirmPassword;
}
