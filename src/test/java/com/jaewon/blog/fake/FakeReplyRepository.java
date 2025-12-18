package com.jaewon.blog.fake;

import com.jaewon.blog.entity.reply.Reply;
import com.jaewon.blog.entity.reply.ReplyState;
import com.jaewon.blog.repository.ReplyRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class FakeReplyRepository implements ReplyRepository {
    private final Map<Long, Reply> map = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0L);

    @Override
    public Mono<Void> deleteAllByUserId(Long userId) {
        return Mono.fromRunnable(() -> map.values().stream()
                .filter(reply -> reply.getUserId().equals(userId))
                .forEach(reply -> map.remove(reply.getId(), reply)));
    }

    @Override
    public Mono<Void> deleteAllByPostId(Long postId) {
        return Mono.fromRunnable(() -> map.values().stream()
                .filter(reply -> reply.getPostId().equals(postId))
                .forEach(reply -> map.remove(reply.getId(), reply)));
    }

    @Override
    public Mono<Reply> save(Reply reply) {
        return Mono.fromCallable(() -> {
            long id = sequence.getAndIncrement();

            Reply save = new Reply(
                    id,
                    reply.getContent(),
                    reply.getUserId(),
                    reply.getPostId(),
                    ReplyState.NORMAL,
                    LocalDateTime.now()
            );

            map.put(id, save);
            return save;
        });
    }

    @Override
    public Mono<Void> deleteById(Long replyId) {
        return Mono.fromRunnable(() -> map.remove(replyId));
    }

    @Override
    public Mono<Reply> findById(long replyId) {
        return Mono.fromCallable(() -> Optional.ofNullable(map.get(replyId)))
                .flatMap(optionalReply -> optionalReply.<Mono<? extends Reply>>map(Mono::just)
                        .orElseGet(Mono::empty));
    }
}
