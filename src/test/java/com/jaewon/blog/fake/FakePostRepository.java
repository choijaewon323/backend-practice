package com.jaewon.blog.fake;

import com.jaewon.blog.entity.post.Post;
import com.jaewon.blog.repository.PostRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class FakePostRepository implements PostRepository {
    public final Map<Long, Post> map = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0L);

    @Override
    public Mono<Void> deleteAllByUserId(Long userId) {
        return Mono.fromRunnable(() -> map.values().stream()
                .filter(post -> post.getUserId().equals(userId))
                .forEach(post -> map.remove(post.getId(), post)));
    }

    @Override
    public Mono<Post> save(Post post) {
        return Mono.fromCallable(() -> {
            if (post.getId() != null && map.containsKey(post.getId())) {
                Long id = post.getId();
                map.put(id, post);
                return post;
            }

            Post save = new Post(sequence.get(), post.getTitle(), post.getContent(), post.getUserId(), post.getCategoryId(), LocalDateTime.now());
            map.put(sequence.getAndIncrement(), save);

            return save;
        });
    }

    @Override
    public Mono<Post> findById(Long postId) {
        return Mono.fromCallable(() -> Optional.ofNullable(map.get(postId)))
                .flatMap(Mono::justOrEmpty);
    }

    @Override
    public Mono<Void> deleteById(Long postId) {
        return Mono.fromRunnable(() -> map.remove(postId));
    }

    public Mono<Void> deleteAll() {
        return Mono.fromRunnable(map::clear);
    }
}
