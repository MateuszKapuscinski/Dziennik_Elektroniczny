package com.sda.jz75_security_template.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Dyplom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @ToString.Exclude
    @JsonBackReference
    private Klasa klasaPole;

    @ManyToOne()
    @ToString.Exclude
    @JsonBackReference
    private Uczen uczenDyplom;

    private Double sredniaOcen;

    private String opis;

    @Enumerated(value = EnumType.STRING)
    private TypWyroznienia typWyroznienia;


}
