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

    public Klasa dodajKlase(Klasa klasa) {
        if (!isValid(klasa)){
            throw new UnsupportedOperationException("Klasa nie jest poprawna!");
        }
        return klasaRepository.save(klasa);
    }

    public boolean isValid(Klasa klasa) {
        return Objects.nonNull(klasa.getNazwa()) &&
                Objects.nonNull(klasa.getPoziom_klasy());
    }

    public void dodajNauczycielaDoKlasy(Long nauczycielId, Klasa nowaKlasa){
        Optional<Nauczyciel> optionalNauczyciel = nauczycielRepository.findById(nauczycielId);
        if (optionalNauczyciel.isPresent()){
            Nauczyciel nauczyciel = optionalNauczyciel.get();

            nowaKlasa.setWychowawca(nauczyciel);
            klasaRepository.save(nowaKlasa);
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

   public void aktualizujDaneKlasy(Long id, Klasa daneAktualizujące){
        Optional<Klasa> optionalKlasa = klasaRepository.findById(id);
        if (optionalKlasa.isPresent()) {
            Klasa edytowanaKlasa = optionalKlasa.get();

            edytowanaKlasa.setNazwa(daneAktualizujące.getNazwa());
            edytowanaKlasa.setPoziom_klasy(daneAktualizujące.getPoziom_klasy());
            edytowanaKlasa.setRocznik(daneAktualizujące.getRocznik());
            edytowanaKlasa.setWychowawca(daneAktualizujące.getWychowawca());
            edytowanaKlasa.setUczniowie(daneAktualizujące.getUczniowie());
            edytowanaKlasa.setNumer_sali(daneAktualizujące.getNumer_sali());

            klasaRepository.save(edytowanaKlasa);
        }
   }
}
