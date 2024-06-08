package com.ll.slog2.domain.post.post.entity;

import com.ll.slog2.domain.member.member.entity.Member;
import com.ll.slog2.global.jpa.entity.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
@Getter
@Setter
public class Post extends BaseTime {
    private String title;
    @Column(columnDefinition = "TEXT")
    private String body;
    @ManyToOne
    private Member author;
    @Column(columnDefinition = "BOOLEAN default false")
    private boolean published;
    @Column(columnDefinition = "BOOLEAN default false")
    private boolean listed;
}