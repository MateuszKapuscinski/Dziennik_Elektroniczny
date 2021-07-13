package com.sda.jz75_security_template.model.account;

import com.sda.jz75_security_template.model.Nauczyciel;
import com.sda.jz75_security_template.model.Uczen;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    @OneToOne(optional = true)
    private Nauczyciel nauczyciel;

    @OneToOne(optional = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Uczen uczen;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<AccountRole> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(accountRole -> new SimpleGrantedAuthority(accountRole.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) &&
                Objects.equals(username, account.username) &&
                Objects.equals(password, account.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }
}
