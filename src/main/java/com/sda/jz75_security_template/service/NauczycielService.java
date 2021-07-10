package com.sda.jz75_security_template.service;

import com.sda.jz75_security_template.model.Nauczyciel;
import com.sda.jz75_security_template.model.Przedmiot;
import com.sda.jz75_security_template.repository.NauczycielRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NauczycielService {
    private final NauczycielRepository nauczycielRepository;

    private boolean isValid(Nauczyciel nauczyciel) {
        return Objects.nonNull(nauczyciel) &&
                Objects.nonNull(nauczyciel.getImie()) &&
                Objects.nonNull(nauczyciel.getNazwisko()) &&
                Objects.nonNull(nauczyciel.getEmail()) &&
                Objects.nonNull(nauczyciel.getPrzedmiot());
    }

    public void dodajNauczyciela(Nauczyciel nauczyciel) {
        if (!isValid(nauczyciel)) {
            return;
        }
        nauczycielRepository.save(nauczyciel);
    }

    public void usunNauczyciela(Long id) {
        nauczycielRepository.deleteById(id);
    }

    public Optional<Nauczyciel> zwrocNauczycielaPoId(Long id) {
        return nauczycielRepository.findById(id);
    }

    public boolean usunNauczycielaPoJegoId(Long id) {
        Optional<Nauczyciel> optionalNauczyciel = zwrocNauczycielaPoId(id);
        if (optionalNauczyciel.isPresent()) {
            usunNauczycielaPoJegoId(id);
            return true;
        }
        return false;
    }

    public List<Nauczyciel> zwrocWszystkich() {
        return nauczycielRepository.findAll();
    }
}
