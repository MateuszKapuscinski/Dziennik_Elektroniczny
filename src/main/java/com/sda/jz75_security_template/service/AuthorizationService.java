package com.sda.jz75_security_template.service;

import com.sda.jz75_security_template.model.account.Account;
import com.sda.jz75_security_template.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorizationService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username (parametr) to jest nazwa użytkownika wpisana w formularzu
        Optional<Account> optionalAccount = accountRepository.findByUsername(username);
        if (optionalAccount.isPresent()) {

            return optionalAccount.get();
        }
        throw new UsernameNotFoundException("Can't find " + username);
    }
}
