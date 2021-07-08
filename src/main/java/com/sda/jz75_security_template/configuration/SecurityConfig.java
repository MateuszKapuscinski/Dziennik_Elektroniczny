package com.sda.jz75_security_template.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService authorizationService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/webjars/**",
                        "/css/**",
                        "/js/**").permitAll()
                .antMatchers(HttpMethod.POST, "/teacher/register/student").hasRole("SUPERVISOR")
                .antMatchers(HttpMethod.GET, "/teacher/register/student").hasRole("SUPERVISOR")
                .antMatchers(HttpMethod.POST, "/admin/register/teacher").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/admin/register/teacher").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                    .formLogin()
                        .loginPage("/login")
                        .defaultSuccessUrl("/authenticated", true)
                        .permitAll()
                .and()
                    .logout()
                        .logoutUrl("/logout")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Tworzymy sobie klasę obsługi logowania
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);          // definiujemy szyfrator
        daoAuthenticationProvider.setUserDetailsService(authorizationService);  // dostarczamy klasę która sprawdza obiekty w BD

        auth.authenticationProvider(daoAuthenticationProvider); // ustawiamy obiekt autoryzacji
    }
}
