package com.sda.jz75_security_template.service;

import com.sda.jz75_security_template.exception.InvalidRegisterData;
import com.sda.jz75_security_template.model.Nauczyciel;
import com.sda.jz75_security_template.model.Uczen;
import com.sda.jz75_security_template.model.account.*;
import com.sda.jz75_security_template.repository.AccountRepository;
import com.sda.jz75_security_template.repository.AccountRoleRepository;
import com.sda.jz75_security_template.repository.NauczycielRepository;
import com.sda.jz75_security_template.repository.UczenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.sda.jz75_security_template.configuration.DataInitializer.*;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final NauczycielRepository nauczycielRepository;
    private final PasswordEncoder passwordEncoder;
    private final UczenRepository uczenRepository;

    public List<Account> getAccountList() {
        return accountRepository.findAll();
    }


    public boolean deleteAccount(Long accountId) {
        if (accountRepository.existsById(accountId)) {
            accountRepository.deleteById(accountId);
            return true;
        }
        return false;
    }

/*    public boolean deleteTeacherAccount(Long accountId, Long nauczycielId){
        if (accountRepository.existsById(accountId)){
            accountRepository.deleteById(accountId);
            nauczycielRepository.deleteById(nauczycielId);
        }
        return false;
    }*/

    public Optional<Account> findAccount(Long accountId) {
        return accountRepository.findById(accountId);
    }

    public void updateAccount(Account account, RolesDto roles) {
        Optional<Account> accountOptional = accountRepository.findById(account.getId());
        if (accountOptional.isPresent()) {
            Account editedAccount = accountOptional.get();

            editedAccount.setEnabled(account.isEnabled());
            editedAccount.setAccountNonLocked(account.isAccountNonLocked());
            if (account.getPassword() != null && !account.getPassword().isEmpty()) {
                editedAccount.setPassword(passwordEncoder.encode(account.getPassword()));
            }

            checkAndUpdateRole(editedAccount, ROLE_ADMIN, roles.isAdmin());
            checkAndUpdateRole(editedAccount, ROLE_SUPERVISOR, roles.isSupervisor());
            checkAndUpdateRole(editedAccount, ROLE_USER, roles.isUser());

            accountRepository.save(editedAccount);
        }
    }

    private void checkAndUpdateRole(Account editedAccount, String roleName, boolean shouldHaveAuthority) {
        if (editedAccount.getRoles().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getName().equals(roleName))) {
            if (!shouldHaveAuthority) {
                Optional<AccountRole> optionalAccountRole = accountRoleRepository.findByName(roleName);
                if (optionalAccountRole.isPresent()) {
                    AccountRole accountRole = optionalAccountRole.get();
                    editedAccount.getRoles().remove(accountRole);
                }
            }
            return;
        }
        if (shouldHaveAuthority) {
            Optional<AccountRole> optionalAccountRole = accountRoleRepository.findByName(roleName);
            if (optionalAccountRole.isPresent()) {
                AccountRole accountRole = optionalAccountRole.get();
                editedAccount.getRoles().add(accountRole);
            }
        }
        // jeśli nie return'ował to znaczy że roli nie ma
    }

    public boolean registerTeacher(CreateTeacherAccountRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidRegisterData("Passwords do not match!");
        }

        Optional<Account> accountOptional = accountRepository.findByUsername(request.getUsername());
        if (accountOptional.isPresent()) {
            throw new InvalidRegisterData("Account with given username already exists!");
        }

        Nauczyciel nauczyciel = nauczycielRepository.save(Nauczyciel.builder()
                .imie(request.getImie())
                .nazwisko(request.getNazwisko())
                .email(request.getEmail())
                .przedmiot(request.getPrzedmiot())
                .build());

        Account account = Account.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .roles(new HashSet<>())
                .nauczyciel(nauczyciel)
                .build();

        checkAndUpdateRole(account, ROLE_SUPERVISOR, true);

        accountRepository.save(account);
        return true;
    }

    public boolean registerStudent(CreateStudentAccountRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidRegisterData("Passwords do not match!");
        }

        Optional<Account> accountOptional = accountRepository.findByUsername(request.getUsername());
        if (accountOptional.isPresent()) {
            throw new InvalidRegisterData("Account with given username already exists!");
        }

        Uczen uczen = uczenRepository.save(Uczen.builder()
                .imie(request.getImie())
                .nazwisko(request.getNazwisko())
                .email(request.getEmail())
                .data_urodzenia(request.getData_urodzenia())
                .build());


        Account account = Account.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .roles(new HashSet<>())
                .uczen(uczen)
                .build();

        checkAndUpdateRole(account, ROLE_USER, true);

        accountRepository.save(account);
        return true;
    }

    public void aktualizujDaneKontaNauczyciela(Long id, Account account, Nauczyciel daneNauczyciela) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()) {
            Account editedAccount = accountOptional.get();

            editedAccount.setUsername(account.getUsername());
            if(!account.getPassword().isEmpty()) {
                editedAccount.setPassword(account.getPassword());
            }

            editedAccount.setNauczyciel(daneNauczyciela);
            accountRepository.save(editedAccount);
        }
    }

    public void aktualizujDaneKontaUcznia(Long id, Account account, Uczen daneUcznia){
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()){
            Account editedAccount = accountOptional.get();
            editedAccount.setUsername(account.getUsername());
            if (!account.getPassword().isEmpty()){
                editedAccount.setPassword(account.getPassword());
            }
            editedAccount.setUczen(daneUcznia);
            accountRepository.save(editedAccount);
        }
    }
}
