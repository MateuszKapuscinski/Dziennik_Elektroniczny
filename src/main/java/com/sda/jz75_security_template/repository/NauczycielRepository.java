package com.sda.jz75_security_template.repository;

import com.sda.jz75_security_template.model.Nauczyciel;
import com.sda.jz75_security_template.model.Przedmiot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NauczycielRepository extends JpaRepository<Nauczyciel, Long> {
   // List<Nauczyciel> znajdzPoPrzedmiocie(Przedmiot przedmiot);
}
