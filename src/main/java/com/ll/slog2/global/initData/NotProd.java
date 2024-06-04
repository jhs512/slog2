package com.ll.slog2.global.initData;

import com.ll.slog2.domain.member.member.entity.Member;
import com.ll.slog2.domain.member.member.service.MemberService;
import com.ll.slog2.domain.post.post.entity.Post;
import com.ll.slog2.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

@Profile("!prod")
@Configuration
@RequiredArgsConstructor
public class NotProd {
    private final MemberService memberService;
    private final PostService postService;
    @Lazy
    @Autowired
    private NotProd self;

    @Bean
    @Order(4)
    public ApplicationRunner initNotProd() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        if (postService.count() > 0) return;

        Member memberUser1 = memberService.join("user1", "1234", "유저1").getData();
        memberUser1.setRefreshToken("user1");

        Member memberUser2 = memberService.join("user2", "1234", "유저2").getData();
        memberUser2.setRefreshToken("user2");

        Post post1 = postService.write(memberUser1, "제목 1", "내용 1").getData();
        Post post2 = postService.write(memberUser1, "제목 2", "내용 2").getData();

        Post post3 = postService.write(memberUser2, "제목 3", "내용 3").getData();
        Post post4 = postService.write(memberUser2, "제목 4", "내용 4").getData();
    }
}