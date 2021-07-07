package com.sda.jz75_security_template.configuration;

import com.sda.jz75_security_template.model.account.Account;
import com.sda.jz75_security_template.model.account.AccountRole;
import com.sda.jz75_security_template.repository.AccountRepository;
import com.sda.jz75_security_template.repository.AccountRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Klasa inicjalizuje dane i metoda onApplicationEvent jest wywołana RAZ w momencie załdowania aplikacji.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {
    private final static String ADMIN_USERNAME = "admin";
    private final static String ADMIN_PASSWORD = "nimda";

    public final static String ROLE_ADMIN = "ROLE_ADMIN";
    public final static String ROLE_USER = "ROLE_USER";
    public final static String ROLE_SUPERVISOR = "ROLE_SUPERVISOR";

    private final static String[] AVAILABLE_ROLES = {ROLE_ADMIN, ROLE_USER, ROLE_SUPERVISOR};

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        for (String role : AVAILABLE_ROLES) {
            addRole(role);
        }

        addUser(ADMIN_USERNAME, ADMIN_PASSWORD, AVAILABLE_ROLES);
        addUser("user", "resu", new String[] {ROLE_USER});
    }

    private void addUser(String username, String password, String[] roles) {
        Optional<Account> optionalAccount = accountRepository.findByUsername(username);
        if (!optionalAccount.isPresent()) {

            Account account = Account.builder()
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .enabled(true)
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .build();

            Set<AccountRole> rolesSet = new HashSet<>();
            for (String role : roles) {
                Optional<AccountRole> optionalAccountRole = accountRoleRepository.findByName(role);
                if(optionalAccountRole.isPresent()){
                    AccountRole accountRole = optionalAccountRole.get();
                    rolesSet.add(accountRole);
                }
            }

            account.setRoles(rolesSet);

            accountRepository.save(account);
        }
    }

    private void addRole(String role) {
        Optional<AccountRole> optionalAccountRole = accountRoleRepository.findByName(role);
        if (!optionalAccountRole.isPresent()) {
            AccountRole accountRole = AccountRole.builder()
                    .name(role)
                    .build();

            accountRoleRepository.save(accountRole);
        }
    }
}
