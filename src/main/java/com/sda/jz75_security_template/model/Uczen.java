package com.sda.jz75_security_template.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Builder
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

    @OneToMany(mappedBy = "uczen",fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    private Set<Dyplom> dyplomy;

    @ManyToMany(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private Set<Klasa> klasy;

    //dalem lazy bo wywalało błąd
    @OneToMany(mappedBy = "uczen", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private Set<Ocena> oceny;
}
