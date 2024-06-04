package com.ll.slog2.domain.member.member.controller;

import com.ll.slog2.domain.auth.auth.service.AuthTokenService;
import com.ll.slog2.standard.util.Ut;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class ApiV1MemberControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private AuthTokenService authTokenService;

    @Test
    @DisplayName("POST /api/v1/members/login")
    void t1() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/members/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                                {
                                                    "username": "user1",
                                                    "password": "1234"
                                                }
                                                """
                                )
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1MemberController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(cookie().exists("accessToken"))
                .andExpect(cookie().exists("refreshToken"));

        String accessToken = Arrays.stream(resultActions.andReturn().getResponse().getCookies())
                .filter(cookie -> cookie.getName().equals("accessToken"))
                .findFirst()
                .orElseThrow()
                .getValue();

        Map<String, Object> accessTokenData = authTokenService.getDataFrom(accessToken);

        assertThat(accessTokenData.get("username")).isEqualTo("user1");
    }

    @Test
    @DisplayName("POST /api/v1/members")
    void t2() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                                {
                                                    "username": "user100",
                                                    "password": "1234",
                                                    "nickname": "유저100"
                                                }
                                                """
                                )
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1MemberController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(jsonPath("$.data.item.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.item.createDate", matchesPattern(Ut.date.DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.modifyDate", matchesPattern(Ut.date.DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.username", notNullValue()))
                .andExpect(jsonPath("$.data.item.nickname", notNullValue()));
    }

    @Test
    @DisplayName("GET /api/v1/members/me, with user 1")
    @WithUserDetails("user1")
    void t3() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/members/me")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1MemberController.class))
                .andExpect(handler().methodName("getMe"))
                .andExpect(jsonPath("$.data.item.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.item.createDate", matchesPattern(Ut.date.DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.modifyDate", matchesPattern(Ut.date.DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.username", is("user1")))
                .andExpect(jsonPath("$.data.item.nickname", notNullValue()));
    }

    @Test
    @DisplayName("GET /api/v1/members/me, with user 1")
    @WithUserDetails("user2")
    void t4() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/members/me")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1MemberController.class))
                .andExpect(handler().methodName("getMe"))
                .andExpect(jsonPath("$.data.item.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.item.createDate", matchesPattern(Ut.date.DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.modifyDate", matchesPattern(Ut.date.DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.username", is("user2")))
                .andExpect(jsonPath("$.data.item.nickname", notNullValue()));
    }

    @Test
    @DisplayName("GET /api/v1/members/me, 로그인 상태가 아니라면 이용할 수 없음")
    void t5() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/members/me")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /api/v1/members/me, with cookie")
    void t6() throws Exception {
        // WHEN
        MockHttpServletRequestBuilder requestBuilder = get("/api/v1/members/me")
                .cookie(new Cookie("accessToken", "EMPTY"))
                .cookie(new Cookie("refreshToken", "user1"));

        ResultActions resultActions = mvc
                .perform(requestBuilder)
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1MemberController.class))
                .andExpect(handler().methodName("getMe"))
                .andExpect(jsonPath("$.data.item.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.item.createDate", matchesPattern(Ut.date.DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.modifyDate", matchesPattern(Ut.date.DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.username", is("user1")))
                .andExpect(jsonPath("$.data.item.nickname", notNullValue()));
    }

    @Test
    @DisplayName("DELETE /api/v1/members/logout")
    void t7() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(
                        delete("/api/v1/members/logout")
                )
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1MemberController.class))
                .andExpect(handler().methodName("logout"))
                .andExpect(cookie().exists("accessToken"))
                .andExpect(cookie().exists("refreshToken"));

        String accessToken = Arrays.stream(resultActions.andReturn().getResponse().getCookies())
                .filter(cookie -> cookie.getName().equals("accessToken"))
                .findFirst()
                .orElseThrow()
                .getValue();

        String refreshToken = Arrays.stream(resultActions.andReturn().getResponse().getCookies())
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findFirst()
                .orElseThrow()
                .getValue();

        assertThat(accessToken).isEmpty();
        assertThat(refreshToken).isEmpty();
    }

    @Test
    @DisplayName("GET /api/v1/members")
    @WithUserDetails("admin")
    void t8() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/api/v1/members"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1MemberController.class))
                .andExpect(handler().methodName("getItems"))
                .andExpect(jsonPath("$.data.items[0].id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.items[0].createDate", matchesPattern(Ut.date.DATE_PATTERN)))
                .andExpect(jsonPath("$.data.items[0].modifyDate", matchesPattern(Ut.date.DATE_PATTERN)))
                .andExpect(jsonPath("$.data.items[0].username", notNullValue()))
                .andExpect(jsonPath("$.data.items[0].nickname", notNullValue()));
    }

    @Test
    @DisplayName("GET /api/v1/members/1")
    @WithUserDetails("admin")
    void t9() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/api/v1/members/1"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1MemberController.class))
                .andExpect(handler().methodName("getItem"))
                .andExpect(jsonPath("$.data.item.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.item.createDate", matchesPattern(Ut.date.DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.modifyDate", matchesPattern(Ut.date.DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.username", notNullValue()))
                .andExpect(jsonPath("$.data.item.nickname", notNullValue()));
    }

    @Test
    @DisplayName("GET /api/v1/members/1, 관리자 권한이 없으면 실패")
    @WithUserDetails("user1")
    void t10() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/api/v1/members/1"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isForbidden());
    }
}
