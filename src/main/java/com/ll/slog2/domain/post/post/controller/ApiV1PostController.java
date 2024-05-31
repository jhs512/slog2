package com.ll.slog2.domain.post.post.controller;

import com.ll.slog2.domain.post.post.dto.PostDto;
import com.ll.slog2.domain.post.post.entity.Post;
import com.ll.slog2.domain.post.post.service.PostService;
import com.ll.slog2.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "ApiV1PostController", description = "Post CRUD 컨트롤러")
public class ApiV1PostController {
    private final PostService postService;

    public record GetPostsResBody(@NonNull List<PostDto> items) {
    }

    @GetMapping
    @Operation(summary = "다건 조회")
    public RsData<GetPostsResBody> getPosts() {
        List<Post> posts = postService.findAll();

        return RsData.of(
                new GetPostsResBody(
                        posts.stream().map(PostDto::new).toList()
                )
        );
    }
}
