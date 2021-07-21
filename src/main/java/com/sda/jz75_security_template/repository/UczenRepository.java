package com.sda.jz75_security_template.repository;

import com.sda.jz75_security_template.model.Uczen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public interface UczenRepository extends JpaRepository<Uczen, Long> {
  //  Set<Uczen> znajdzWszystkich()
}
