package com.sda.jz75_security_template.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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

    //lub string
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataUrodzenia;

    @OneToMany(mappedBy = "uczenDyplom",fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonBackReference
    private List<Dyplom> ListaDyplomow;


    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Klasa> klasaSet;

    //dalem lazy bo wywalało błąd
    @OneToMany(mappedBy = "uczenOcena", fetch = FetchType.LAZY)
    private List<Ocena> ocenaList;
}
