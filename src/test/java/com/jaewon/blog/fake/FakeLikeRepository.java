package com.jaewon.blog.fake;

import com.jaewon.blog.entity.like.Like;
import com.jaewon.blog.entity.like.LikeType;
import com.jaewon.blog.repository.LikeRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class FakeLikeRepository implements LikeRepository {
    public final Map<Long, Like> map = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0L);

    @Override
    public Mono<Like> findByUserIdAndTargetIdAndType(Long userId, Long targetId, LikeType type) {
        return Mono.fromCallable(() -> map.values().stream()
                .filter(value -> value.getTargetId().equals(targetId) && value.getUserId().equals(userId) && value.getType() == type)
                .findFirst())
                .flatMap(Mono::justOrEmpty);
    }

    @Override
    public Mono<Void> deleteAllByUserId(Long userId) {
        return Mono.fromRunnable(() -> map.values().stream()
                .filter(like -> Objects.equals(like.getUserId(), userId))
                .forEach(like -> map.remove(like.getId(), like)));
    }

    @Override
    public Mono<Like> save(Like like) {
        return Mono.fromCallable(() -> {
            if (like.getId() != null && map.containsKey(like.getId())) {
                map.put(like.getId(), like);

                return like;
            }
            Like save = new Like(sequence.get(), like.getUserId(), like.getTargetId(), like.getType(), like.getIsLiked(), LocalDateTime.now());

            map.put(sequence.getAndIncrement(), save);

            return save;
        });
    }
}
