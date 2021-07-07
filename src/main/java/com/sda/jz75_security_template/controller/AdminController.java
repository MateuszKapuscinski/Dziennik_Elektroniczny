package com.sda.jz75_security_template.controller;

import com.sda.jz75_security_template.model.account.Account;
import com.sda.jz75_security_template.model.account.RolesDto;
import com.sda.jz75_security_template.service.AccountService;
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

    @GetMapping
    public String getIndex() {
        return "admin-index";
    }

    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        if(principal instanceof UsernamePasswordAuthenticationToken){
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
            if(usernamePasswordAuthenticationToken.getPrincipal() instanceof Account) {
                Account account = (Account) usernamePasswordAuthenticationToken.getPrincipal();
                model.addAttribute("username", account.getUsername());
            }
        }
    }

    @GetMapping("/accounts")
    public String getAccounts(Model model, @RequestParam(required = false) String error) {
        model.addAttribute("accounts", accountService.getAccountList());
        model.addAttribute("error_msg", error);
        return "admin-account-list";
    }

    @GetMapping("/account/delete/{accountId}")
    public String deleteAccount(Model model, @PathVariable Long accountId, HttpServletRequest request) {
        boolean success = accountService.deleteAccount(accountId);
        if (success) {
            return "redirect:" + request.getHeader("referer");
        }
        return "redirect:/accounts?error=Unable to delete account";
    }

    @GetMapping("/account/edit/{accountId}")
    public String editAccount(Model model, @PathVariable Long accountId) {
        Optional<Account> accountOptional = accountService.findAccount(accountId);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            boolean isUser = account.getRoles().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getName().equals(ROLE_USER));
            boolean isSupervisor = account.getRoles().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getName().equals(ROLE_SUPERVISOR));
            boolean isAdmin = account.getRoles().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getName().equals(ROLE_ADMIN));

            model.addAttribute("role_admin", isAdmin);
            model.addAttribute("role_supervisor", isSupervisor);
            model.addAttribute("role_user", isUser);
            model.addAttribute("account_to_edit", accountOptional.get());
            return "admin-account-edit";
        }
        return "redirect:/accounts?error=Unable to delete account";
    }

    @PostMapping("/account/edit")
    public String submitAccount(Account account, boolean admin, boolean supervisor, boolean user) {
        accountService.updateAccount(account, new RolesDto(admin, supervisor, user));
        return "redirect:/admin/accounts";
    }
}