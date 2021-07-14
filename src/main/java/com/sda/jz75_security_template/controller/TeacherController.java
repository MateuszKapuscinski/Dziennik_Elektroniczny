package com.sda.jz75_security_template.controller;

import com.sda.jz75_security_template.exception.InvalidRegisterData;
import com.sda.jz75_security_template.model.Klasa;
import com.sda.jz75_security_template.model.PoziomKlasy;
import com.sda.jz75_security_template.model.Uczen;
import com.sda.jz75_security_template.model.account.CreateStudentAccountRequest;
import com.sda.jz75_security_template.repository.UczenRepository;
import com.sda.jz75_security_template.service.AccountService;
import com.sda.jz75_security_template.service.KlasaService;
import com.sda.jz75_security_template.service.UczenService;
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
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/teacher")
@PreAuthorize(value = "hasRole('SUPERVISOR')")
@RequiredArgsConstructor
public class TeacherController {
    private final AccountService accountService;
    private final UczenService uczenService;
    private final KlasaService klasaService;

    @GetMapping("/register/student")
    public String getRegisterPage(){
        return "register-student";
    }

    @PostMapping("/register/student")
    public String submitRegisterPage(Model model, CreateStudentAccountRequest request){
        try{
            boolean success = accountService.registerStudent(request);
            if(success) {
                return "redirect:/login";
            }
        }catch (InvalidRegisterData ird){
            model.addAttribute("error_msg", ird.getMessage());
            model.addAttribute("prev_user", request.getUsername());
        }
        return "register-student";
    }

    @GetMapping("/klasy")
    public String tworzenieKlasy(Model model) {
        Klasa klasa = new Klasa();
        model.addAttribute("nowa_klasa" + klasa);
        model.addAttribute("poziom_klasy", PoziomKlasy.values());
        return "teacher-dodaj-klase";
    }

    @PostMapping("/klasy")
    public String tworzenieKlasyPost(Klasa klasa) {
        klasaService.dodajKlase(klasa);
        return "redirect:/teacher/lista";
    }

    @GetMapping("/lista")
    public String wyswietlKlasy(Model model) {
        List<Klasa> listaKlas = klasaService.zwrocListeKlas();
        model.addAttribute("lista_klas",listaKlas);
        return "teacher-lista-klas";
    }

    @GetMapping("/usun/klasy")
    public String usuwanieKlasy(@RequestParam(name = "id")Long id) {
        klasaService.usunKlase(id);
        return "redirect:/teacher/lista";
    }

/*    @PostMapping("/klasy")
    public String dodajKlasy(Long nauczycielId, Long uczenId, Long klasaId) {
        klasaService.dodajNauczycielaDoKlasy(klasaId,nauczycielId);
        klasaService.dodajUczniaDoKlasy(klasaId,uczenId);
        return "redirect:/authenticated";
    }

    @GetMapping("/klasy")
    public String dodajKlasyGet(Model model, Long nauczycielId, Long uczenId){
        model.addAttribute("dodaj_nauczyciela" + nauczycielId);
        model.addAttribute("dodaj_ucznia" + uczenId);
        return "teacher-dodaj-klase";
    }*/
}
