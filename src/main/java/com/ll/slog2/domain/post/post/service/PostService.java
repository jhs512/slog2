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
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

    public long count() {
        return postRepository.count();
    }

    @Transactional
    public RsData<Post> write(Member author, String title, String body, boolean published, boolean listed) {
        Post post = Post
                .builder()
                .author(author)
                .title(title)
                .body(body)
                .published(published)
                .published(published)
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
    public RsData<Post> modify(Post post, String title, String body, boolean published, boolean listed) {
        post.setTitle(title);
        post.setBody(body);
        post.setPublished(published);
        post.setListed(listed);

        return RsData.of("%d번 글이 수정되었습니다.".formatted(post.getId()), post);
    }

    @Transactional
    public RsData<Post> findTempOrMake(Member author) {
        AtomicBoolean isNew = new AtomicBoolean(false);

        Post post = postRepository.findTop1ByAuthorAndPublishedAndTitleOrderByIdDesc(
                author,
                false,
                "임시글"
        ).orElseGet(() -> {
            isNew.set(true);
            return write(author, "임시글", "", false, false).getData();
        });

        return RsData.of(
                isNew.get() ? "임시글(%d번)이 생성되었습니다.".formatted(post.getId()) : "%d번 임시글을 불러왔습니다.".formatted(post.getId()),
                post
        );
    }
}