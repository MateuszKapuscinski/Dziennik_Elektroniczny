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
        }else {
            log.error("nie udało się dodać oceny");
        }
    }

    public Optional<Ocena> zwrocOcenaPoId(Long ocenaId){
        return ocenaRepository.findById(ocenaId);
    }

    public void usunOcene(Long id) {
        ocenaRepository.deleteById(id);
    }

    public void aktualizujDaneOceny(Long id, Ocena ocena_zaktualizowana){
        Optional<Ocena> optionalOcena = ocenaRepository.findById(id);
        if (optionalOcena.isPresent()) {
            Ocena edytowanaOcena = optionalOcena.get();

            edytowanaOcena.setOcenaWartosc(ocena_zaktualizowana.getOcenaWartosc());
            edytowanaOcena.setPrzedmiot(ocena_zaktualizowana.getPrzedmiot());

            ocenaRepository.save(edytowanaOcena);
        }
    }
}
