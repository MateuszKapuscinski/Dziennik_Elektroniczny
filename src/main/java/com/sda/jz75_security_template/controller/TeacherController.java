package com.sda.jz75_security_template.controller;

import com.sda.jz75_security_template.exception.InvalidRegisterData;
import com.sda.jz75_security_template.model.*;
import com.sda.jz75_security_template.model.account.CreateStudentAccountRequest;
import com.sda.jz75_security_template.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/teacher")
@PreAuthorize(value = "hasRole('SUPERVISOR')")
@RequiredArgsConstructor
public class TeacherController {
    private final AccountService accountService;
    private final UczenService uczenService;
    private final KlasaService klasaService;
    private final OcenaService ocenaService;
    private final NauczycielService nauczycielService;

    @GetMapping("lista/kont")
    public String wyswietlListeKont(Model model, @RequestParam(required = false) String error) {
        model.addAttribute("error_msg", error);
        model.addAttribute("accounts", accountService.getAccountList()
                .stream().filter(account -> account.getUczen() != null)
                .collect(Collectors.toList()));
        return "teacher-account-uczen";
    }

    @GetMapping("/register/student")
    public String getRegisterPage() {
        return "register-student";
    }

    @PostMapping("/register/student")
    public String submitRegisterPage(Model model, CreateStudentAccountRequest request) {
        try {
            boolean success = accountService.registerStudent(request);
            if (success) {
                return "redirect:/login";
            }
        } catch (InvalidRegisterData ird) {
            model.addAttribute("error_msg", ird.getMessage());
            model.addAttribute("prev_user", request.getUsername());
        }
        return "register-student";
    }

    @GetMapping("/klasy")
    public String tworzenieKlasy(Model model) {
        Nauczyciel nauczyciel = new Nauczyciel();
        Klasa klasa = new Klasa();
        model.addAttribute("dane_nauczyciela", nauczyciel);
        model.addAttribute("nowa_klasa", klasa);
        model.addAttribute("poziom_klasy", PoziomKlasy.values());
        return "teacher-dodaj-klase";
    }

    @PostMapping("/klasy")
    public String tworzenieKlasyPost(Klasa klasa, Long nauczycielId) {
        Klasa nowaKlasa = klasaService.dodajKlase(klasa);
        klasaService.dodajNauczycielaDoKlasy(nauczycielId, klasa);

        return "redirect:/teacher/lista";
    }

    @GetMapping("/lista")
    public String wyswietlKlasy(Model model) {
        List<Klasa> listaKlas = klasaService.zwrocListeKlas();
        model.addAttribute("lista_klas", listaKlas);
        return "teacher-lista-klas";
    }

    @GetMapping("/usun/klasy")
    public String usuwanieKlasy(@RequestParam(name = "id") Long id) {
        klasaService.usunKlase(id);
        return "redirect:/teacher/lista";
    }

    @GetMapping("/edytuj")
    public String edytujKlase(Model model, @RequestParam(name = "id_klasa") Long id) {
        Optional<Klasa> optionalKlasa = klasaService.zwrocKlasePoId(id);
        if (optionalKlasa.isPresent()) {
            model.addAttribute("klasaId", id);
            model.addAttribute("edytowana_klasa", optionalKlasa.get());
            model.addAttribute("poziom_klasy", PoziomKlasy.values());
            return "teacher-edit-klasa";
        }
        return "redirect:/teacher/lista";
    }

    @PostMapping("/edytuj")
    public String edytujKlasePost(Long klasaId, Klasa klasa) {
        klasaService.aktualizujDaneKlasy(klasaId, klasa);
        return "redirect:/teacher/lista";
    }

    @GetMapping("/uczniowie")
    public String wyswietlListeUczniow(Model model) {
        List<Uczen> uczenList = uczenService.zwrocWszystkichUczniow();
        model.addAttribute("lista_uczniow", uczenList);
        return "uczen-lista";
    }

    @GetMapping("/dodaj/ocene")
    public String dodajOcene(Model model, Long idUcznia) {
        Ocena nowaOcena = new Ocena();
        nowaOcena.setOcena(1);

        model.addAttribute("nowa_ocena", nowaOcena);
        model.addAttribute("oceny_z_przedmiotow", Przedmiot.values());
        model.addAttribute("uczenId", idUcznia);
        return "uczen-dodaj-ocena";
    }

    @PostMapping("/dodaj/ocene")
    public String dodajOcenePost(Ocena ocena, Long id_ucznia) {
        ocenaService.dodajOceneDoUcznia(ocena, id_ucznia);
        return "redirect:/teacher/szczegoly?id_ucznia=" + id_ucznia;
    }

    @GetMapping("/szczegoly")
    public String szczegolyUcznia(Model model, Long id_ucznia) {
        Optional<Uczen> uczenOptional = uczenService.zwrocUczniaPoId(id_ucznia);
        if (uczenOptional.isPresent()) {
            model.addAttribute("szczegoly_ucznia",uczenOptional.get());
            return "szczegoly-ucznia";
        }
        return "redirect:/teacher/uczniowie";
    }

    @GetMapping("/usun/ocene")
    public String usuwanieOceny(Long ocenaId, Long uczenId){
        ocenaService.usunOcene(ocenaId);
        return "redirect:/teacher/szczegoly?id_ucznia=" + uczenId;
    }
}


