package com.sda.jz75_security_template.controller;

import com.sda.jz75_security_template.model.account.Account;
import com.sda.jz75_security_template.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class Index {

   @GetMapping
    public String getIndex(){
        return "redirect:/authenticated";
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

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
