package com.jaewon.blog.repository;

import com.jaewon.blog.entity.post.Post;
import reactor.core.publisher.Mono;

public interface PostRepository {
    Mono<Void> deleteAllByUserId(Long userId);
    Mono<Post> save(Post post);
    Mono<Post> findById(Long postId);
    Mono<Void> deleteById(Long postId);
}
