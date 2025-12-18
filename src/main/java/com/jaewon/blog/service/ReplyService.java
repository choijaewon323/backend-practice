package com.jaewon.blog.service;

import com.jaewon.blog.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;

    public Mono<Void> deleteByReplyId(Long replyId) {
        return replyRepository.deleteById(replyId)
                .then();
    }

    public Mono<Void> deleteAllRepliesByUserId(Long userId) {
        return replyRepository.deleteAllByUserId(userId);
    }

    public Mono<Boolean> deleteAllRepliesByPostId(Long postId) {
        return replyRepository.deleteAllByPostId(postId)
                .thenReturn(true)
                .onErrorReturn(false);
    }

    public Mono<Void> updateReportedState(int reportedCount, long replyId) {
        return replyRepository.findById(replyId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("해당 댓글이 없습니다")))
                .flatMap(reply -> {
                    if (reply.isBanned()) {
                        return Mono.empty();
                    }

                    if (reply.isBannedLimit(reportedCount)) {
                        reply.ban();
                    }

                    return replyRepository.save(reply);
                })
                .then();
    }
}
