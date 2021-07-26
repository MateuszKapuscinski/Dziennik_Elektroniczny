package com.sda.jz75_security_template.controller;

import com.sda.jz75_security_template.model.Ocena;
import com.sda.jz75_security_template.model.Uczen;
import com.sda.jz75_security_template.model.account.Account;
import com.sda.jz75_security_template.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;
import java.util.OptionalDouble;

@Slf4j
@Controller
@RequestMapping("/student")
@PreAuthorize(value = "hasRole('USER')")
@RequiredArgsConstructor
public class StudentController {
    private final UczenService uczenService;
    private final AccountService accountService;

    @GetMapping()
    public String wyswietlDaneUcznia(Model model, Principal principal) {
        Account account = AuthorizationService.getCurrentUserAccount(principal);
        if (account.getUczen() == null) {
            throw new UnsupportedOperationException("Not allowed, page only for students.");
        }
        Uczen uczen = account.getUczen();

        OptionalDouble srednia = uczenService.pobierzOcenyUcznia(uczen)
                .stream()
                .mapToDouble(Ocena::getOcenaWartosc)
                .average();

        String sredniaKomunikat = srednia.isPresent() ? String.valueOf(srednia.getAsDouble()) : "Brak ocen";

        model.addAttribute("dane_ucznia", uczen);
        model.addAttribute("oceny_ucznia", uczenService.pobierzOcenyUcznia(uczen));
        model.addAttribute("oceny_ucznia_srednia", sredniaKomunikat);
        model.addAttribute("dyplomy_ucznia", uczenService.pobierzDyplomyUcznia(uczen));
        model.addAttribute("klasy_ucznia", uczenService.pobierzKlasyUcznia(uczen));
        model.addAttribute("konto_ucznia", account);
        return "uczen-dane-koncowe";
    }
}
