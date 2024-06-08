package com.ll.slog2.domain.auth.auth.service;

import com.ll.slog2.domain.member.member.entity.Member;
import com.ll.slog2.domain.post.post.entity.Post;
import com.ll.slog2.global.exceptions.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    // Post 관련 시작
    public void checkCanGetPost(Member actor, Post post) {
        if (!canGetPost(actor, post))
            throw new GlobalException("403-1", "권한이 없습니다.");
    }

    public boolean canGetPost(Member actor, Post post) {
        if (actor == null) return false;
        if (post == null) return false;

        return actor.equals(post.getAuthor());
    }


    public void checkCanDeletePost(Member actor, Post post) {
        if (!canDeletePost(actor, post))
            throw new GlobalException("403-1", "권한이 없습니다.");
    }

    public boolean canDeletePost(Member actor, Post post) {
        return canGetPost(actor, post);
    }


    public void checkCanModifyPost(Member actor, Post post) {
        if (!canModifyPost(actor, post))
            throw new GlobalException("403-1", "권한이 없습니다.");
    }

    public boolean canModifyPost(Member actor, Post post) {
        return canGetPost(actor, post);
    }
    // Post 관련 끝
}