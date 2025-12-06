package com.jaewon.blog.repository;

import com.jaewon.blog.entity.like.Like;
import com.jaewon.blog.entity.like.LikeType;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface LikeRepository extends ReactiveCrudRepository<Like, Long> {
    Mono<Like> findByTargetIdAndType(Long targetId, LikeType type);
    Mono<Void> deleteAllByUserId(Long userId);
}
