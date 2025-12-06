package com.jaewon.blog.repository;

import com.jaewon.blog.entity.reply.Reply;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ReplyRepository extends ReactiveCrudRepository<Reply, Long> {
    Mono<Void> deleteAllByUserId(Long userId);
    Mono<Void> deleteAllByPostId(Long postId);
}
