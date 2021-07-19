package com.sda.jz75_security_template.service;

import com.sda.jz75_security_template.model.Nauczyciel;
import com.sda.jz75_security_template.model.Przedmiot;
import com.sda.jz75_security_template.model.account.Account;
import com.sda.jz75_security_template.repository.AccountRepository;
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
    private final AccountRepository accountRepository;


 /*   public List<Account> zwrocListeKontUczniow(Long id){
        List<>
        accountRepository.
    }*/


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
        if (nauczycielRepository.existsById(id)) {
            nauczycielRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Nauczyciel> zwrocWszystkich() {
        return nauczycielRepository.findAll();
    }

    public Nauczyciel aktualizujDaneNauczyciela(Long id, Nauczyciel nauczyciel) {
        // znaleźć nauczyciela
        // edycja: imie, nazwisko, mail, przedmiot
        // zapisać nauczyciela
        Optional<Nauczyciel> optionalNauczyciel = nauczycielRepository.findById(id);
        if (optionalNauczyciel.isPresent()){
            Nauczyciel edytowany_nauczyciel = optionalNauczyciel.get();

            edytowany_nauczyciel.setImie(nauczyciel.getImie());
            edytowany_nauczyciel.setNazwisko(nauczyciel.getNazwisko());
            edytowany_nauczyciel.setEmail(nauczyciel.getEmail());
            edytowany_nauczyciel.setPrzedmiot(nauczyciel.getPrzedmiot());
            return nauczycielRepository.save(edytowany_nauczyciel);
        }
        throw new UnsupportedOperationException("Coś poszło nietak!");
        // zwracamy już edytowanego nauczyciela (metoda repository.save zwraca obiekt który ma być zwrócony)
    }
}
