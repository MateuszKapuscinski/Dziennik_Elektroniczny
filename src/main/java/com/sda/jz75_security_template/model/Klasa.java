package com.sda.jz75_security_template.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Klasa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "klasy",fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private Set<Uczen> uczniowie;

    @OneToMany(mappedBy = "klasa",fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Dyplom> dyplomy;

    @ManyToOne()
    @ToString.Exclude
    @JsonBackReference
    private Nauczyciel wychowawca;

    @Enumerated(value = EnumType.STRING)
    private PoziomKlasy poziom_klasy;

    private int rocznik;

    private String nazwa;

    private  int numer_sali;


}
