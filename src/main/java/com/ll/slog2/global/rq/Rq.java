package com.ll.slog2.global.rq;

import com.ll.slog2.domain.auth.auth.service.AuthTokenService;
import com.ll.slog2.domain.member.member.entity.Member;
import com.ll.slog2.domain.member.member.service.MemberService;
import com.ll.slog2.global.app.AppConfig;
import com.ll.slog2.global.security.SecurityUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequestScope
@RequiredArgsConstructor
public class Rq {
    private final HttpServletRequest req;
    private final HttpServletResponse resp;

    private final MemberService memberService;
    private final AuthTokenService authTokenService;

    @PersistenceContext
    private EntityManager entityManager;
    private SecurityUser user;
    private Member member;
    private Boolean isLogin;
    private Boolean isAdmin;

    public Member getMember() {
        if (isLogout()) return null;

        if (member == null) {
            // entityManager 객체로 프록시 객체 얻기
            member = entityManager.getReference(Member.class, getUser().getId());
        }

        return member;
    }

    public boolean isAdmin() {
        if (isLogout()) return false;

        if (isAdmin == null) {
            isAdmin = getUser()
                    .getAuthorities()
                    .stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        }

        return isAdmin;
    }

    public boolean isLogout() {
        return !isLogin();
    }

    public boolean isLogin() {
        if (isLogin == null) getUser();

        return isLogin;
    }

    private SecurityUser getUser() {
        if (isLogin == null) {
            user = Optional.ofNullable(SecurityContextHolder.getContext())
                    .map(context -> context.getAuthentication())
                    .filter(authentication -> authentication.getPrincipal() instanceof SecurityUser)
                    .map(authentication -> (SecurityUser) authentication.getPrincipal())
                    .orElse(null);

            isLogin = user != null;
        }

        return user;
    }

    public void setLogin(long id, String username, List<String> authorities) {
        SecurityUser securityUser = new SecurityUser(
                id,
                username,
                "",
                authorities.stream().map(SimpleGrantedAuthority::new).toList()
        );
        SecurityContextHolder.getContext().setAuthentication(securityUser.genAuthentication());
    }

    // SESSION 관련 시작
    public void destroySession() {
        req.getSession().invalidate();
    }

    // URL 관련 시작
    public String getCurrentUrlPath() {
        return req.getRequestURI();
    }

    public boolean isFrontUrl(String url) {
        return url.startsWith(AppConfig.getSiteFrontUrl());
    }

    // HTTP 관련 시작
    public void setStatusCode(int statusCode) {
        resp.setStatus(statusCode);
    }


    // 쿠키관련 시작
    public String getCookieValue(String cookieName, String defaultValue) {
        if (req.getCookies() == null) return defaultValue;

        return Arrays.stream(req.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(defaultValue);
    }

    public void removeCookie(String name) {
        ResponseCookie cookie = ResponseCookie.from(name)
                .path("/")
                .maxAge(0)
                .domain(getSiteCookieDomain())
                .sameSite("Strict")
                .secure(true)
                .httpOnly(true)
                .build();

        resp.addHeader("Set-Cookie", cookie.toString());
    }

    public void setCookie(String name, String value, long maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .maxAge(maxAge)
                .domain(getSiteCookieDomain())
                .sameSite("Strict")
                .secure(true)
                .httpOnly(true)
                .build();

        resp.addHeader("Set-Cookie", cookie.toString());
    }

    public void setCookie(String name, String value) {
        setCookie(name, value, 60 * 60 * 24 * 365 * 10);
    }

    private String getSiteCookieDomain() {
        String cookieDomain = AppConfig.getSiteCookieDomain();

        if (cookieDomain.equals("localhost")) return cookieDomain;

        return "." + cookieDomain;
    }

    public void setAuthTokenCookie(Member member) {
        String accessToken = authTokenService.genToken(member, AppConfig.getAccessTokenExpirationSec());

        setCookie("accessToken", accessToken);
        setCookie("refreshToken", member.getRefreshToken());
    }

    public void removeAuthTokenCookie() {
        removeCookie("accessToken");
        removeCookie("refreshToken");
    }
    // 쿠키관련 끝
}
