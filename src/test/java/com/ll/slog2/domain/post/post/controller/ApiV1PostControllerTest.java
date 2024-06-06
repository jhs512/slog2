package com.ll.slog2.domain.post.post.controller;

import com.ll.slog2.standard.util.Ut;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class ApiV1PostControllerTest {
    @Autowired
    private MockMvc mvc;


    @Test
    @DisplayName("GET /api/v1/posts")
    void t1() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/api/v1/posts"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(handler().methodName("getItems"))
                .andExpect(jsonPath("$.data.items[0].id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.items[0].createDate", matchesPattern(Ut.date.DATE_PATTERN)))
                .andExpect(jsonPath("$.data.items[0].modifyDate", matchesPattern(Ut.date.DATE_PATTERN)))
                .andExpect(jsonPath("$.data.items[0].authorId", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.items[0].authorName", notNullValue()))
                .andExpect(jsonPath("$.data.items[0].title", notNullValue()))
                .andExpect(jsonPath("$.data.items[0].body", notNullValue()));
    }

    @Test
    @DisplayName("GET /api/v1/posts/1")
    void t2() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/api/v1/posts/1"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(handler().methodName("getItem"))
                .andExpect(jsonPath("$.data.item.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.item.createDate", matchesPattern(Ut.date.DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.modifyDate", matchesPattern(Ut.date.DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.authorId", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.item.authorName", notNullValue()))
                .andExpect(jsonPath("$.data.item.title", notNullValue()))
                .andExpect(jsonPath("$.data.item.body", notNullValue()));
    }

    @Test
    @DisplayName("PUT /api/v1/posts/1")
    @WithUserDetails("user1")
    void t3() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(
                        put("/api/v1/posts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                                {
                                                    "title": "제목 1 new",
                                                    "body": "내용 1 new"
                                                }
                                                """
                                )
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(handler().methodName("modify"))
                .andExpect(jsonPath("$.data.item.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.item.createDate", matchesPattern(Ut.date.DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.modifyDate", matchesPattern(Ut.date.DATE_PATTERN)))
                .andExpect(jsonPath("$.data.item.authorId", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data.item.authorName", notNullValue()))
                .andExpect(jsonPath("$.data.item.title", is("제목 1 new")))
                .andExpect(jsonPath("$.data.item.body", is("내용 1 new")));
    }
}
