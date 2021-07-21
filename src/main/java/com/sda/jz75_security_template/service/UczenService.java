package com.sda.jz75_security_template.service;

import com.sda.jz75_security_template.model.Klasa;
import com.sda.jz75_security_template.model.Uczen;
import com.sda.jz75_security_template.repository.KlasaRepository;
import com.sda.jz75_security_template.repository.UczenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UczenService {
    private final UczenRepository uczenRepository;
    private final KlasaRepository klasaRepository;
    private final KlasaService klasaService;

    public boolean isValidUczen(Uczen uczen) {
        return Objects.nonNull(uczen) &&
                Objects.nonNull(uczen.getImie()) &&
                Objects.nonNull(uczen.getNazwisko()) &&
                Objects.nonNull(uczen.getEmail()) &&
                Objects.nonNull(uczen.getData_urodzenia());
    }

    public void dodajUcznia(Uczen uczen) {
        if (!isValidUczen(uczen)) {
            return;
        }
        uczenRepository.save(uczen);
    }

    public Optional<Uczen> zwrocUczniaPoId(Long id) {
        return uczenRepository.findById(id);
    }

    public List<Uczen> zwrocWszystkichUczniow() {
        return uczenRepository.findAll();
    }

    public Uczen aktualizujDaneUcznia(Long id, Uczen uczen) {
        Optional<Uczen> uczenOptional = uczenRepository.findById(id);
        if (uczenOptional.isPresent()) {
            Uczen edytowanyUczen = uczenOptional.get();

            edytowanyUczen.setImie(uczen.getImie());
            edytowanyUczen.setNazwisko(uczen.getNazwisko());
            edytowanyUczen.setEmail(uczen.getEmail());
            edytowanyUczen.setData_urodzenia(uczen.getData_urodzenia());
            return uczenRepository.save(edytowanyUczen);
        }
        throw new UnsupportedOperationException("Coś poszło nietak!");
    }

    public boolean usunUczniaPoJegoId(Long id) {
        if (uczenRepository.existsById(id)){
            uczenRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void usunUcznia(Long id){
        uczenRepository.deleteById(id);
    }

    public List<Uczen> zwrocWszystkichUczniowSet() {
        return uczenRepository.findAll();
    }

   public void dodajUczniaDoKlasy(Long id_ucznia, Long idKlasy) {
        Optional<Klasa> optionalKlasa = klasaRepository.findById(idKlasy);
        Optional<Uczen> optionalUczen = uczenRepository.findById(id_ucznia);
        if (optionalKlasa.isPresent() && optionalUczen.isPresent()){
            Klasa klasa = optionalKlasa.get();
            Uczen uczen = optionalUczen.get();

            uczen.getKlasy().add(klasa);
            uczenRepository.save(uczen);
        }
    }
}
