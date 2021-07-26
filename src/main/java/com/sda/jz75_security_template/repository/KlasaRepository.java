package com.sda.jz75_security_template.repository;

import com.sda.jz75_security_template.model.Klasa;
import com.sda.jz75_security_template.model.Nauczyciel;
import com.sda.jz75_security_template.model.Uczen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KlasaRepository extends JpaRepository<Klasa, Long> {
    List<Klasa> findAllByWychowawca(Nauczyciel poleNauczyciel);
    List<Klasa> findAllByUczniowieContaining(Uczen uczen);

}
