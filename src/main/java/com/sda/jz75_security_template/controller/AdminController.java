package com.sda.jz75_security_template.controller;

import com.sda.jz75_security_template.exception.InvalidRegisterData;
import com.sda.jz75_security_template.model.Nauczyciel;
import com.sda.jz75_security_template.model.Przedmiot;
import com.sda.jz75_security_template.model.account.Account;
import com.sda.jz75_security_template.model.account.CreateTeacherAccountRequest;
import com.sda.jz75_security_template.model.account.RolesDto;
import com.sda.jz75_security_template.service.AccountService;
import com.sda.jz75_security_template.service.NauczycielService;

import com.sda.jz75_security_template.service.UczenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.sda.jz75_security_template.configuration.DataInitializer.*;

@Slf4j
@Controller
@RequestMapping("/admin")
// ######################################################################################
//@Secured("ROLE_ADMIN")                    // musi mieć koniecznie tą rolę
@PreAuthorize(value = "hasRole('ADMIN')")   // to jest równoważne linii wyżej
// ######################################################################################
//@PreAuthorize(value = "hasAnyRole('ADMIN', 'SUPERVISOR')") // musi mieć którąkolwiek z ról, są dwie akceptowalne
@RequiredArgsConstructor
public class AdminController {
    private final AccountService accountService;
    private final NauczycielService nauczycielService;
    private final UczenService uczenService;

    @GetMapping
    public String getIndex() {
        return "admin-index";
    }

    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        if (principal instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
            if (usernamePasswordAuthenticationToken.getPrincipal() instanceof Account) {
                Account account = (Account) usernamePasswordAuthenticationToken.getPrincipal();
                model.addAttribute("username", account.getUsername());
            }
        }
    }

    @GetMapping("/accounts")
    public String getAccounts(Model model, @RequestParam(required = false) String error) {
        model.addAttribute("accounts", accountService.getAccountList()
                .stream()
                .filter(account -> account.getUczen() == null)
                .filter(account -> account.getNauczyciel() != null)
                .collect(Collectors.toList()));
        // wysyłana jest lista wszystkich użytkowników którzy nie są uczniami
        model.addAttribute("error_msg", error);
        return "admin-account-list";
    }

    @GetMapping("/account/delete/{account_id}/{nauczyciel_id}")
    public String deleteAccount(@PathVariable Long account_id, HttpServletRequest request, @PathVariable Long nauczyciel_id) {
        boolean success = accountService.deleteAccount(account_id);
        boolean succes2 = nauczycielService.usunNauczycielaPoJegoId(nauczyciel_id);
        if (success && succes2) {
            return "redirect:" + request.getHeader("referer");
        }
        return "redirect:/accounts?error=Unable to delete account";
    }

    @GetMapping("/register/teacher")
    public String getRegisterPage(Model model) {
        model.addAttribute("przedmioty", Przedmiot.values());
        return "register-teacher";
    }

    @PostMapping("/register/teacher")
    public String submitRegisterPage(Model model, CreateTeacherAccountRequest request) {
        try {
            boolean success = accountService.registerTeacher(request);
            if (success) {
                return "redirect:/login";
            }
        } catch (InvalidRegisterData ird) {
            model.addAttribute("error_msg", ird.getMessage());
            model.addAttribute("prev_user", request.getUsername());
        }
        return "register-teacher";
    }

    @GetMapping("/account/edit/nauczyciel")
    public String edycjaNauczyciela(Model model, @RequestParam(name = "id_nauczyciel") Long id) {
        Optional<Nauczyciel> optionalNauczyciel = nauczycielService.zwrocNauczycielaPoId(id);
        if (optionalNauczyciel.isPresent()) {
            Nauczyciel nauczyciel = optionalNauczyciel.get();
            Account account = nauczyciel.getAccount();
            model.addAttribute("konto_nauczyciela", account);
            model.addAttribute("przedmioty", Przedmiot.values());
            log.info("Nauczyciel do edycji" + optionalNauczyciel);
            return "teacher-account-edit";
        }
        return "redirect:/admin/accounts";
    }

    @PostMapping("/account/edit/nauczyciel")
    public String edycjaNauczyciela(Account account) {
        Nauczyciel daneNauczyciela = nauczycielService.aktualizujDaneNauczyciela(account.getNauczyciel().getId(), account.getNauczyciel());
        accountService.aktualizujDaneKontaNauczyciela(account.getId(), account, daneNauczyciela);
        return "redirect:/admin/accounts";
    }

}