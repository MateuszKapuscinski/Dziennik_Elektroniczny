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
public class Ocena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @ToString.Exclude
    @JsonBackReference
    private Uczen poleUczen;

    private int ocena;

    @Enumerated(value = EnumType.STRING)
    private Przedmiot przedmiot;
}
