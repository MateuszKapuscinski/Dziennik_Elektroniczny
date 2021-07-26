package com.sda.jz75_security_template.repository;

import com.sda.jz75_security_template.model.Dyplom;
import com.sda.jz75_security_template.model.Ocena;
import com.sda.jz75_security_template.model.Uczen;
import com.sda.jz75_security_template.service.DyplomService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DyplomRepository extends JpaRepository<Dyplom,Long> {
    List<Dyplom> findAllByUczen(Uczen uczen);
}
