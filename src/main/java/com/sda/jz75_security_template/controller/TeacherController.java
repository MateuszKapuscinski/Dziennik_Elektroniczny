package com.sda.jz75_security_template.controller;

import com.sda.jz75_security_template.exception.InvalidRegisterData;
import com.sda.jz75_security_template.model.*;
import com.sda.jz75_security_template.model.account.Account;
import com.sda.jz75_security_template.model.account.CreateStudentAccountRequest;
import com.sda.jz75_security_template.model.account.RolesDto;
import com.sda.jz75_security_template.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.sda.jz75_security_template.configuration.DataInitializer.*;

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
    private final DyplomService dyplomService;

    @GetMapping("lista/kont")
    public String wyswietlListeKont(Model model, @RequestParam(required = false) String error) {
        model.addAttribute("error_msg", error);
        model.addAttribute("accounts", accountService.getAccountList()
                .stream().filter(account -> account.getUczen() != null)
                .collect(Collectors.toList()));
        return "teacher-account-list";
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

    @GetMapping("/account/delete/uczen/{account_id}/{uczen_id}")
    public String deleteAccountStudent(@PathVariable Long account_id, HttpServletRequest request, @PathVariable Long uczen_id) {
        boolean success = accountService.deleteAccount(account_id);
        boolean succes2 = uczenService.usunUczniaPoJegoId(uczen_id);
        if (success && succes2) {
            return "redirect:" + request.getHeader("referer");
        }
        return "redirect:/accounts?error=Unable to delete account";
    }

    @GetMapping("/account/edit/uczen")
    public String edycjaUcznia(Model model, @RequestParam(name = "id_uczen") Long id) {
        Optional<Uczen> optionalUczen = uczenService.zwrocUczniaPoId(id);
        if (optionalUczen.isPresent()) {
            Uczen uczen = optionalUczen.get();
            Account account = uczen.getAccount();
            model.addAttribute("konto_ucznia", account);
            log.info("Uczen do edycji" + optionalUczen);
            return "teacher-account-uczen";
        }
        return "redirect:/teacher/lista/kont";
    }

    @PostMapping("/account/edit/uczen")
    public String edycjaUczniaPost(Account account) {
        Uczen daneUcznia = uczenService.aktualizujDaneUcznia(account.getUczen().getId(), account.getUczen());
        accountService.aktualizujDaneKontaUcznia(account.getId(), account, daneUcznia);
        return "redirect:/teacher/lista/kont";
    }

    @GetMapping("/klasy")
    public String tworzenieKlasy(Model model) {
        Nauczyciel nauczyciel = new Nauczyciel();
        Klasa klasa = new Klasa();
        model.addAttribute("dane_nauczyciela", nauczyciel);
        model.addAttribute("nowa_klasa", klasa);
        model.addAttribute("poziom_klasy", PoziomKlasy.values());
        model.addAttribute("wszyscyNauczyciele", nauczycielService.zwrocWszystkich());
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
        model.addAttribute("nowa_ocena", new Ocena());
        model.addAttribute("oceny_z_przedmiotow", Przedmiot.values());
        model.addAttribute("uczenId", idUcznia);
        return "uczen-dodaj-ocena";
    }

    @PostMapping("/dodaj/ocene")
    public String dodajOcenePost(Ocena ocena, Long id_ucznia) {
        log.info("Ocena przed dodaniem po odebraniu: " + ocena);
        ocenaService.dodajOceneDoUcznia(ocena, id_ucznia);
        return "redirect:/teacher/szczegoly?id_ucznia=" + id_ucznia;
    }

    @GetMapping("/szczegoly")
    public String szczegolyUcznia(Model model, Long id_ucznia) {
        Optional<Uczen> uczenOptional = uczenService.zwrocUczniaPoId(id_ucznia);
        if (uczenOptional.isPresent()) {
            model.addAttribute("szczegoly_ucznia", uczenOptional.get());
            return "szczegoly-ucznia";
        }
        return "redirect:/teacher/uczniowie";
    }

    @GetMapping("/usun/ocene")
    public String usuwanieOceny(Long ocenaId, Long uczenId) {
        ocenaService.usunOcene(ocenaId);
        return "redirect:/teacher/szczegoly?id_ucznia=" + uczenId;
    }

    @GetMapping("/edytuj/ocene")
    public String edytujOcene(Model model, @RequestParam(name = "ocena_id") Long ocena_id, Long id_ucznia) {
        Optional<Ocena> optionalOcena = ocenaService.zwrocOcenaPoId(ocena_id);
        if (optionalOcena.isPresent()) {
            model.addAttribute("ocenaId", ocena_id);
            model.addAttribute("edytowana_ocena", optionalOcena.get());
            model.addAttribute("edytowany_przedmiot", Przedmiot.values());
            model.addAttribute("uczenId", id_ucznia);
            return "uczen-edytuj-ocena";
        }
        return "redirect:/teacher/szczegoly?id_ucznia=" + id_ucznia;
    }

    @PostMapping("/edytuj/ocene")
    public String edytujOcenePost(Long id_oceny, Ocena edytowanaOcena) {
        ocenaService.aktualizujDaneOceny(id_oceny, edytowanaOcena);
        return "redirect:/teacher/szczegoly?id_ucznia=" + edytowanaOcena.getUczen().getId();
    }

    @GetMapping("/dodaj/dyplom")
    public String dodajDyplom(Model model,Long idUcznia){
        model.addAttribute("nowy_dyplom", new Dyplom());
        model.addAttribute("typ_wyroznienia", TypWyroznienia.values());
        model.addAttribute("uczenId",idUcznia);
        return "uczen-dodaj-dyplom";
    }

    @PostMapping("/dodaj/dyplom")
    public String dodajDyplomPost(Dyplom dyplom,Long id_ucznia){
        dyplomService.dodajDyplomDoUcznia(dyplom,id_ucznia);
        return "redirect:/teacher/szczegoly?id_ucznia=" + id_ucznia;
    }

    @GetMapping("/usun/dyplom")
    public String usuwanieDyplomu(Long dyplomId, Long uczenId){
        dyplomService.usunDyplom(dyplomId);
        return "redirect:/teacher/szczegoly?id_ucznia=" + uczenId;
    }

    @GetMapping("/edytuj/dyplom")
    public String edytujDyplom(Model model, @RequestParam(name = "dyplom_id") Long dyplom_id, Long id_ucznia){
        Optional<Dyplom> optionalDyplom = dyplomService.zwrocDyplomPoId(dyplom_id);
        if (optionalDyplom.isPresent()){
            model.addAttribute("dyplomId",dyplom_id);
            model.addAttribute("edytowany_dyplom", optionalDyplom.get());
            model.addAttribute("edytowany_typWyroznienia", TypWyroznienia.values());
            model.addAttribute("uczenId",id_ucznia);
            return "uczen-edytuj-dyplom";
        }
        return "redirect:/teacher/szczegoly?id_ucznia=" + id_ucznia;
    }

    @PostMapping("edytuj/dyplom")
    public String edytujDyplomPost(Long id_dyplom, Dyplom edytowanyDyplom){
        dyplomService.aktualizujDyplom(id_dyplom,edytowanyDyplom);
        return "redirect:/teacher/szczegoly?id_ucznia=" + edytowanyDyplom.getUczen().getId();
    }
}


