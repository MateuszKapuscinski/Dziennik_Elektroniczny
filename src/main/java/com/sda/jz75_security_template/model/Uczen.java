package com.sda.jz75_security_template.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Uczen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imie;

    private String nazwisko;

    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataUrodzenia;


    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Klasa> klasaSet;

    @OneToMany(mappedBy = "poleUczen", fetch = FetchType.EAGER)
    private List<Ocena> ocenaList;
}
