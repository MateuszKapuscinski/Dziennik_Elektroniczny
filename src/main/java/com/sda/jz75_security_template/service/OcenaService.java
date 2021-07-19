package com.sda.jz75_security_template.service;

import com.sda.jz75_security_template.model.Ocena;
import com.sda.jz75_security_template.model.Uczen;
import com.sda.jz75_security_template.repository.OcenaRepository;
import com.sda.jz75_security_template.repository.UczenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OcenaService {
    private final OcenaRepository ocenaRepository;
    private final UczenRepository uczenRepository;

    public void dodajOceneDoUcznia(Ocena ocena,Long uczenId) {
        Optional<Uczen> uczenOptional = uczenRepository.findById(uczenId);
        if (uczenOptional.isPresent()){
            Uczen uczen = uczenOptional.get();

            ocena.setUczen(uczen);
            ocenaRepository.save(ocena);
        }
        log.error("nie udało się dodać oceny");
    }

    public Optional<Ocena> zwrocOcenaPoId(Long id){
        return ocenaRepository.findById(id);
    }

    public void usunOcene(Long id) {
        ocenaRepository.deleteById(id);
    }
}