package com.ll.slog2.domain.member.member.entity;

import com.ll.slog2.global.jpa.entity.BaseTime;
import com.ll.slog2.standard.util.Ut;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;


@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
@Getter
@Setter
public class Member extends BaseTime {
    @Column(unique = true)
    private String username;
    private String password;
    private String nickname;
    @Column(unique = true)
    private String refreshToken;
    private String profileImgUrl;

    public String getName() {
        return nickname;
    }

    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthoritiesAsStringList()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Transient
    public List<String> getAuthoritiesAsStringList() {
        List<String> authorities = new ArrayList<>();

        if (isAdmin())
            authorities.add("ROLE_ADMIN");

        return authorities;
    }

    @Transient
    public boolean isAdmin() {
        return List.of("system", "admin").contains(getUsername());
    }

    @Transient
    public String getProfileImgUrlOrDefault() {
        return Ut.str.hasLength(profileImgUrl) ? profileImgUrl : "https://placehold.co/640x640?text=O_O";
    }

    @Transient
    public boolean isSocial() {
        return false;
    }
}
