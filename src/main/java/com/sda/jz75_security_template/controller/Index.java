package com.sda.jz75_security_template.controller;

import com.sda.jz75_security_template.exception.InvalidRegisterData;
import com.sda.jz75_security_template.model.account.Account;
import com.sda.jz75_security_template.model.account.CreateAccountRequest;
import com.sda.jz75_security_template.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class Index {
    private final AccountService accountService;

    @GetMapping
    public String getIndex(){
        return "index";
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterPage(){
        return "register";
    }

    @PostMapping("/register")
    public String submitRegisterPage(Model model, CreateAccountRequest request){
        try{
            boolean success = accountService.register(request);
            if(success) {
                return "redirect:/login";
            }
        }catch (InvalidRegisterData ird){
            model.addAttribute("error_msg", ird.getMessage());
            model.addAttribute("prev_user", request.getUsername());
        }
        return "register";
    }

    // MODELS:
    // - ADMIN REJESTRUJE UZYTKOWNIKOW
    // - UZYTKOWNICY REJESTRUJA SIE SAMI -

    // Plan:
    // - rejestracja uzytkownika


    @GetMapping("/authenticated")
    public String getAuthenticated(Model model, Principal principal){
        if(principal instanceof UsernamePasswordAuthenticationToken){
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
            if(usernamePasswordAuthenticationToken.getPrincipal() instanceof Account) {
                Account account = (Account) usernamePasswordAuthenticationToken.getPrincipal();
                model.addAttribute("uzytkownik", account);
            }
        }
        return "authenticated";
    }
}
