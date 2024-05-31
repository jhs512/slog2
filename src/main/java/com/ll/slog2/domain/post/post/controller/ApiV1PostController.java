package com.ll.slog2.domain.post.post.controller;

import com.ll.slog2.domain.post.post.dto.PostDto;
import com.ll.slog2.domain.post.post.entity.Post;
import com.ll.slog2.domain.post.post.service.PostService;
import com.ll.slog2.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApiV1PostController {
    private final PostService postService;

    public record GetPostsResBody(List<PostDto> items) {
    }

    @GetMapping
    public RsData<GetPostsResBody> getPosts() {
        List<Post> posts = postService.findAll();

        return RsData.of(
                new GetPostsResBody(
                        posts.stream().map(PostDto::new).toList()
                )
        );
    }
}
