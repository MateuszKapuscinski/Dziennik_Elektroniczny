package com.sda.jz75_security_template.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Nauczyciel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "wychowawca", fetch = FetchType.LAZY)
    private Set<Klasa> klasy;

    private String imie;

    private String nazwisko;

    private String email;

    @Enumerated(value = EnumType.STRING)
    private Przedmiot przedmiot;

}
