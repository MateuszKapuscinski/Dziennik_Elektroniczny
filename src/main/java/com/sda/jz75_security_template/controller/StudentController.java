package com.sda.jz75_security_template.controller;

import com.sda.jz75_security_template.model.Uczen;
import com.sda.jz75_security_template.model.account.Account;
import com.sda.jz75_security_template.service.AccountService;
import com.sda.jz75_security_template.service.UczenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/student")
@PreAuthorize(value = "hasRole('USER')")
@RequiredArgsConstructor
public class StudentController {
    private final UczenService uczenService;
    private final AccountService accountService;

    @GetMapping()
    public String wyswietlDaneUcznia(Model model, Long idUcznia) {
        Optional<Uczen> uczenOptional = uczenService.zwrocUczniaPoId(idUcznia);
        if (uczenOptional.isPresent()){
            Uczen uczen = uczenOptional.get();
            Account account = uczen.getAccount();
            model.addAttribute("dane_ucznia",uczen);
            model.addAttribute("oceny_ucznia",uczen.getOceny());
            model.addAttribute("dyplomy_ucznia",uczen.getDyplomy());
            model.addAttribute("klasy_ucznia",uczen.getKlasy());
            model.addAttribute("konto_ucznia",account);
            return "uczen-dane-koncowe";
        }
        return "redirect:/student?idUcznia=" + idUcznia;
    }
}
