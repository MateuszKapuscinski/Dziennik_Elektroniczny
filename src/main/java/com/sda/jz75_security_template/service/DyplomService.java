package com.sda.jz75_security_template.service;

import com.sda.jz75_security_template.model.Dyplom;
import com.sda.jz75_security_template.model.Uczen;
import com.sda.jz75_security_template.repository.DyplomRepository;
import com.sda.jz75_security_template.repository.UczenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DyplomService {
    private final DyplomRepository dyplomRepository;
    private final UczenRepository uczenRepository;

    public Optional<Dyplom> zwrocDyplomPoId(Long dyplomId){
        return dyplomRepository.findById(dyplomId);
    }

    public void usunDyplom(Long dyplomId){
        dyplomRepository.deleteById(dyplomId);
    }

    public void dodajDyplomDoUcznia(Dyplom dyplom, Long uczenId){
        Optional<Uczen> uczenOptional = uczenRepository.findById(uczenId);
        if (uczenOptional.isPresent()){
            Uczen uczen = uczenOptional.get();

            dyplom.setUczen(uczen);
            dyplomRepository.save(dyplom);
        }else {
            log.error("nie udało się dodać dyplomu");
        }
    }

    public void aktualizujDyplom(Long id,Dyplom zaktualizowany_dyplom){
        Optional<Dyplom> optionalDyplom = dyplomRepository.findById(id);
        if (optionalDyplom.isPresent()){
            Dyplom edytowany_dyplom = optionalDyplom.get();

            edytowany_dyplom.setOpis(zaktualizowany_dyplom.getOpis());
            edytowany_dyplom.setSredniaOcen(zaktualizowany_dyplom.getSredniaOcen());
            edytowany_dyplom.setTypWyroznienia(zaktualizowany_dyplom.getTypWyroznienia());

            dyplomRepository.save(edytowany_dyplom);
        }
    }
}
