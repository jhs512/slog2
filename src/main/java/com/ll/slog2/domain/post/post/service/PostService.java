package com.ll.slog2.domain.post.post.service;

import com.ll.slog2.domain.member.member.entity.Member;
import com.ll.slog2.domain.post.post.entity.Post;
import com.ll.slog2.domain.post.post.repository.PostRepository;
import com.ll.slog2.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

    public long count() {
        return postRepository.count();
    }

    @Transactional
    public RsData<Post> write(Member author, String title, String body) {
        Post post = Post
                .builder()
                .author(author)
                .title(title)
                .body(body)
                .build();

        postRepository.save(post);

        return RsData.of("%d번 게시물이 작성되었습니다.".formatted(post.getId()), post);
    }

    @Transactional
    public void delete(Post post) {
        postRepository.delete(post);
    }

    public Optional<Post> findById(long id) {
        return postRepository.findById(id);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Transactional
    public RsData<Post> modify(Post post, String title, String body) {
        post.setTitle(title);
        post.setBody(body);

        return RsData.of("%d번 글이 수정되었습니다.".formatted(post.getId()), post);
    }
}