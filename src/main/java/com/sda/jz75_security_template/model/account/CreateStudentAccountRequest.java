package com.sda.jz75_security_template.model.account;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStudentAccountRequest {
    private String username;
    private String password;
    private String confirmPassword;
    private String imie;
    private String nazwisko;
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate data_urodzenia;
}
