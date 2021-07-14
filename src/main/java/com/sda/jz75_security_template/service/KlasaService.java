package com.sda.jz75_security_template.service;

import com.sda.jz75_security_template.model.Klasa;
import com.sda.jz75_security_template.model.Nauczyciel;
import com.sda.jz75_security_template.model.Uczen;
import com.sda.jz75_security_template.repository.KlasaRepository;
import com.sda.jz75_security_template.repository.NauczycielRepository;
import com.sda.jz75_security_template.repository.UczenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class KlasaService {
    private final KlasaRepository klasaRepository;
    private final NauczycielRepository nauczycielRepository;
    private final UczenRepository uczenRepository;

    public Optional<Klasa> zwrocKlasePoId(Long klasaId){
        return klasaRepository.findById(klasaId);
    }

    public List<Klasa> zwrocListeKlas(){
        return klasaRepository.findAll();
    }

    public void usunKlase(Long klasaId){
        klasaRepository.deleteById(klasaId);
    }

    public void dodajKlase(Klasa klasa) {
        if (!isValid(klasa)){
            return;
        }
        klasaRepository.save(klasa);
    }

    public boolean isValid(Klasa klasa) {
        return Objects.nonNull(klasa.getNazwa()) &&
                Objects.nonNull(klasa.getPoziom_klasy()) &&
                Objects.nonNull(klasa.getNumer_sali()) &&
                Objects.nonNull(klasa.getRocznik());
    }

    public void dodajNauczycielaDoKlasy(Long klasaId, Long nauczycielId){
        Optional<Nauczyciel> optionalNauczyciel = nauczycielRepository.findById(nauczycielId);
        Optional<Klasa> klasaOptional = klasaRepository.findById(klasaId);

        if (optionalNauczyciel.isPresent() && klasaOptional.isPresent()){
            Klasa klasa = klasaOptional.get();
            Nauczyciel nauczyciel = optionalNauczyciel.get();

            klasa.setWychowawca(nauczyciel);
            klasaRepository.save(klasa);
        }
        log.error("Nie udało się dodać nauczyciela.");
    }

    public void dodajUczniaDoKlasy(Long klasaId, Long uczenId) {
        Optional<Klasa> klasaOptional = klasaRepository.findById(klasaId);
        Optional<Uczen> uczenOptional = uczenRepository.findById(uczenId);

        if (uczenOptional.isPresent() && klasaOptional.isPresent()){
            Klasa klasa = klasaOptional.get();
            Uczen uczen = uczenOptional.get();

            klasa.getUczniowie().add(uczen);
            klasaRepository.save(klasa);
        }
        log.error("Nie udało się dodać ucznia.");
    }
}
