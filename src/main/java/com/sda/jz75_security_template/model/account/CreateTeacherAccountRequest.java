package com.sda.jz75_security_template.model.account;

import com.sda.jz75_security_template.model.Przedmiot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTeacherAccountRequest {
    private String username;
    private String password;
    private String confirmPassword;
    private String imie;
    private String nazwisko;
    private String email;
    private Przedmiot przedmiot;
}
