package com.sda.jz75_security_template.service;

import com.sda.jz75_security_template.model.Uczen;
import com.sda.jz75_security_template.repository.UczenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UczenService {
    private final UczenRepository uczenRepository;

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

    public Uczen aktualizujDaneNauczyciela(Long id, Uczen uczen) {
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
}
