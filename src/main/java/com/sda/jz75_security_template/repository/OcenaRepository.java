package com.sda.jz75_security_template.repository;

import com.sda.jz75_security_template.model.Klasa;
import com.sda.jz75_security_template.model.Ocena;
import com.sda.jz75_security_template.model.Uczen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OcenaRepository extends JpaRepository<Ocena, Long> {
    List<Ocena> findAllByUczen(Uczen uczen);
}
