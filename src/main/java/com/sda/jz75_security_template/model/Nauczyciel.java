package com.sda.jz75_security_template.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Nauczyciel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "poleNauczyciel", fetch = FetchType.EAGER)
    private List<Klasa> klasaList;

    private String imie;

    private String nazwisko;

    private String email;

    @Enumerated(value = EnumType.STRING)
    private Przedmiot przedmiot;

}
