package com.ll.slog2.domain.post.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApiV1PostController {

    @GetMapping
    public Map<String, Object> getPosts() {
        Map<String, Object> rs = Map.of(
                "data", Map.of(
                        "items", List.of(
                                Map.of(
                                        "id", 1,
                                        "createDate", "2021-08-01T00:00:00",
                                        "modifyDate", "2021-08-01T00:00:00",
                                        "authorId", 1,
                                        "authorName", "author1",
                                        "title", "title1",
                                        "body", "body1"
                                )
                        )));

        return rs;
    }
}
