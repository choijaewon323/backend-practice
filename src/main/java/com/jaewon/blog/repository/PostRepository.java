package com.jaewon.blog.repository;

import com.jaewon.blog.entity.post.Post;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PostRepository extends ReactiveCrudRepository<Post, Long> {
    Mono<Void> deleteAllByUserId(Long userId);
}
