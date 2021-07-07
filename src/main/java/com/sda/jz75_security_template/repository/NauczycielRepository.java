package com.sda.jz75_security_template.repository;

import com.sda.jz75_security_template.model.Nauczyciel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NauczycielRepository extends JpaRepository<Nauczyciel, Long> {
}
