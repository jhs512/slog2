package com.ll.slog2.domain.post.post.dto;

import com.ll.slog2.domain.post.post.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class PostDto {
    @NonNull
    private long id;

    @NonNull
    private LocalDateTime createDate;

    @NonNull
    private LocalDateTime modifyDate;

    @NonNull
    private long authorId;

    @NonNull
    private String authorName;

    @NonNull
    private String title;

    @NonNull
    private String body;

    @NonNull
    private boolean published;

    @NonNull
    private boolean listed;

    @Setter
    private Boolean actorCanRead;

    @Setter
    private Boolean actorCanEdit;

    @Setter
    private Boolean actorCanDelete;

    @Setter
    private Boolean actorCanLike;

    @Setter
    private Boolean actorCanCancelLike;


    public PostDto(Post post) {
        this.id = post.getId();
        this.createDate = post.getCreateDate();
        this.modifyDate = post.getModifyDate();
        this.authorId = post.getAuthor().getId();
        this.authorName = post.getAuthor().getName();
        this.title = post.getTitle();
        this.body = post.getBody();
        this.published = post.isPublished();
        this.listed = post.isListed();
    }
}