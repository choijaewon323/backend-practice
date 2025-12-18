package com.jaewon.blog.repository;

import com.jaewon.blog.entity.reply.Reply;
import reactor.core.publisher.Mono;

public interface ReplyRepository {
    Mono<Void> deleteAllByUserId(Long userId);
    Mono<Void> deleteAllByPostId(Long postId);
    Mono<Reply> save(Reply reply);
    Mono<Void> deleteById(Long replyId);
    Mono<Reply> findById(long replyId);
}
