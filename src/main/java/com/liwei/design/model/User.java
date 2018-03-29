package com.liwei.design.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@NamedQuery(name = "User.findAll", query = "select u from User u")
public class User implements Serializable, UserDetails {

    @Id
    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String auth;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String tiyu;

    @Getter
    @Setter
    private String renwen;

    @Getter
    @Setter
    private String yishu;

    @Getter
    @Setter
    private String lvyou;

    @Getter
    @Setter
    private BigDecimal volume;

    @Getter
    @Setter
    @Column(name = "used_volume")
    private BigDecimal usedVolume;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auths = new ArrayList<>();
        if (this.auth.equals("ADMIN")) {
            auths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            auths.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return auths;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
