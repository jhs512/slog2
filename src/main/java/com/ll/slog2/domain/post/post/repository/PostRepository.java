package com.ll.slog2.domain.post.post.repository;

import com.ll.slog2.domain.member.member.entity.Member;
import com.ll.slog2.domain.post.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findTempByAuthor(Member actor);

    Optional<Post> findTop1ByAuthorAndPublishedAndTitleOrderByIdDesc(Member author, boolean published, String title);
}