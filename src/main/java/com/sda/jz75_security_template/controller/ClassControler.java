package com.sda.jz75_security_template.controller;

import com.sda.jz75_security_template.model.Klasa;
import com.sda.jz75_security_template.model.Uczen;
import com.sda.jz75_security_template.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/klasa")
@PreAuthorize(value = "hasRole('SUPERVISOR')")
@RequiredArgsConstructor
public class ClassControler {
    private final AccountService accountService;
    private final UczenService uczenService;
    private final KlasaService klasaService;
    private final OcenaService ocenaService;
    private final NauczycielService nauczycielService;

    @GetMapping("/szczegoly/{id}")
    public String classDetails(Model model, @PathVariable Long id) {
        Optional<Klasa> optionalKlasa = klasaService.zwrocKlasePoId(id);
        if (optionalKlasa.isPresent()) {
            model.addAttribute("klasa", optionalKlasa.get());
            return "szczegoly-klasy";
        }
        return "redirect:/teacher/lista";
    }

    @GetMapping("/dodaj/uczniow")
    public String dodajUczniow(Model model, @RequestParam Long idKlasy) {
        List<Uczen> uczenList = uczenService.zwrocWszystkichUczniowSet();
        model.addAttribute("uczenList", uczenList);
        model.addAttribute("idKlasy", idKlasy);
        return "klasa-dodaj-ucznia";
    }

   @PostMapping("/dodaj/uczniow")
    public String dodajeUczniowPost(Long id_ucznia, Long idKlasy) {
        log.info("Przesyłamy ucznia do klasy: " + id_ucznia + " klasaId: " + idKlasy);
        uczenService.dodajUczniaDoKlasy(id_ucznia, idKlasy);
        return "redirect:/klasa/szczegoly/" + idKlasy;
    }
}
