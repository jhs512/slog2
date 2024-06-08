package com.ll.slog2.domain.post.post.controller;

import com.ll.slog2.domain.auth.auth.service.AuthService;
import com.ll.slog2.domain.post.post.dto.PostDto;
import com.ll.slog2.domain.post.post.entity.Post;
import com.ll.slog2.domain.post.post.service.PostService;
import com.ll.slog2.global.exceptions.GlobalException;
import com.ll.slog2.global.rq.Rq;
import com.ll.slog2.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "ApiV1PostController", description = "Post CRUD 컨트롤러")
public class ApiV1PostController {
    private final AuthService authService;
    private final PostService postService;
    private final Rq rq;


    private PostDto postToDto(Post post) {
        PostDto dto = new PostDto(post);

        if (rq.isLogin()) {
            dto.setActorCanRead(authService.canGetPost(rq.getMember(), post));
            dto.setActorCanEdit(authService.canModifyPost(rq.getMember(), post));
            dto.setActorCanDelete(authService.canDeletePost(rq.getMember(), post));
        }

        return dto;
    }


    public record PostGetItemsResBody(@NonNull List<PostDto> items) {
    }

    @GetMapping
    @Operation(summary = "다건 조회")
    public RsData<PostGetItemsResBody> getItems() {
        List<Post> posts = postService.findAll();

        return RsData.of(
                new PostGetItemsResBody(
                        posts.stream().map(this::postToDto).toList()
                )
        );
    }


    public record PostGetItemResBody(@NonNull PostDto item) {
    }

    @GetMapping("/{id}")
    @Operation(summary = "단건 조회")
    public RsData<PostGetItemResBody> getItem(
            @PathVariable long id
    ) {
        Post post = postService.findById(id).orElseThrow(GlobalException.E404::new);

        return RsData.of(
                new PostGetItemResBody(
                        postToDto(post)
                )
        );
    }


    public record MakeTempResponseBody(@NonNull PostDto item) {
    }

    @Transactional
    @PostMapping("/temp")
    @Operation(summary = "임시 글 생성")
    public RsData<MakeTempResponseBody> makeTemp() {
        RsData<Post> findTempOrMakeRsData = postService.findTempOrMake(rq.getMember());

        return findTempOrMakeRsData.newDataOf(
                new MakeTempResponseBody(
                        postToDto(findTempOrMakeRsData.getData())
                )
        );
    }


    public record PostModifyReqBody(
            @NotBlank String title,
            @NotBlank String body,
            @NotNull boolean published,
            @NotNull boolean listed
    ) {
    }

    public record PostModifyRespBody(
            @NonNull PostDto item
    ) {
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "수정")
    public RsData<PostModifyRespBody> modify(
            @PathVariable long id,
            @RequestBody @Valid PostModifyReqBody reqBody
    ) {
        Post post = postService.findById(id).orElseThrow(GlobalException.E404::new);

        authService.checkCanModifyPost(rq.getMember(), post);

        RsData<Post> modifyRs = postService.modify(post, reqBody.title, reqBody.body, reqBody.published, reqBody.listed);

        return modifyRs.newDataOf(
                new PostModifyRespBody(
                        new PostDto(modifyRs.getData())
                )
        );
    }
}
