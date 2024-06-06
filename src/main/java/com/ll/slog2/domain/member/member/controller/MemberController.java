package com.ll.slog2.domain.member.member.controller;

import com.ll.slog2.global.rq.Rq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/member")
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Tag(name = "MemberController", description = "스웨거 로그인 용 로그인 폼, 카카오 로그인 기능 등 다양한 기능 제공")
public class MemberController {
    private final Rq rq;

    @GetMapping("/socialLogin/{providerTypeCode}")
    @Operation(summary = "프론트에서 소셜 로그인 후 원래 페이지로 돌아가기 위한 용도로 사용됨, redirectUrl 파라미터를 받아서 쿠키에 저장함")
    public String socialLogin(String redirectUrl, @PathVariable String providerTypeCode) {
        if (rq.isFrontUrl(redirectUrl)) {
            rq.setCookie("redirectUrlAfterSocialLogin", redirectUrl, 60 * 10);
        }

        return "redirect:/oauth2/authorization/" + providerTypeCode;
    }

    @GetMapping("/debugSession")
    @Operation(summary = "세션 디버깅용")
    @ResponseBody
    public Map<String, Object> debugSession(HttpSession httpSession) {
        return StreamSupport.stream(
                        Spliterators.spliteratorUnknownSize(
                                httpSession.getAttributeNames().asIterator(), Spliterator.ORDERED
                        ), false
                )
                .collect(
                        Collectors.toMap(
                                key -> key,
                                key -> httpSession.getAttribute(key)
                        )
                );
    }
}