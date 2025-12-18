package com.jaewon.blog.infra;

import com.jaewon.blog.entity.post.Post;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PostJpaRepository extends ReactiveCrudRepository<Post, Long> {
    Mono<Void> deleteAllByUserId(Long userId);
}
