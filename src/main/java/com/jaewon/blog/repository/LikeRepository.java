package com.jaewon.blog.repository;

import com.jaewon.blog.entity.like.Like;
import com.jaewon.blog.entity.like.LikeType;
import reactor.core.publisher.Mono;

public interface LikeRepository {
    Mono<Like> findByUserIdAndTargetIdAndType(Long userId, Long targetId, LikeType type);
    Mono<Void> deleteAllByUserId(Long userId);
    Mono<Like> save(Like like);
}
