package com.jaewon.blog.fake;

import com.jaewon.blog.entity.User;
import com.jaewon.blog.repository.UserRepository;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class FakeUserRepository implements UserRepository {
    public final Map<Long, User> map = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0L);

    @Override
    public Mono<Long> findIdByEmail(String email) {
        return findByEmail(email)
                .map(User::getId);
    }

    @Override
    public Mono<Long> findIdByNickname(String nickname) {
        return Mono.fromCallable(() -> map.values().stream()
                .filter(user -> user.getNickname().equals(nickname))
                .map(User::getId)
                .findFirst())
                .flatMap(Mono::justOrEmpty);
    }

    @Override
    public Mono<User> save(User user) {
        return Mono.fromCallable(() -> {
            if (user.getId() != null && map.containsKey(user.getId())) {
                map.put(user.getId(), user);

                return user;
            }

            User save = new User(sequence.get(), user.getEmail(), user.getPassword(), user.getNickname(), null);
            map.put(sequence.getAndIncrement(), save);

            return save;
        });
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return Mono.fromCallable(() -> map.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst())
                .flatMap(Mono::justOrEmpty);
    }

    @Override
    public Mono<User> findById(Long userId) {
        return Mono.fromCallable(() -> Optional.ofNullable(map.get(userId)))
                .flatMap(Mono::justOrEmpty);
    }

    @Override
    public Mono<Void> deleteById(Long userId) {
        return Mono.fromRunnable(() -> map.remove(userId));
    }
}
