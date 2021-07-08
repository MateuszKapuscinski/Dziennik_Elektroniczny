package com.sda.jz75_security_template.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    @ManyToMany(mappedBy = "klasaSet",fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonBackReference
    private Set<Uczen> uczenSet;

    @OneToMany(mappedBy = "klasaPole",fetch = FetchType.EAGER)
    private List<Dyplom> ListDyplom;

    @ManyToOne()
    @ToString.Exclude
    @JsonBackReference
    private Nauczyciel poleNauczyciel;

    @Enumerated(value = EnumType.STRING)
    private PoziomKlasy poziomKlasy;

    private int rocznik;

    private String nazwa;

    private  int numerSali;


}
