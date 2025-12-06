package com.jaewon.blog.fake;

import com.jaewon.blog.entity.User;
import com.jaewon.blog.repository.UserRepository;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeUserRepository implements UserRepository {
    public static Map<Long, User> MAP = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Mono<Long> findIdByEmail(String email) {
        return findByEmail(email)
                .map(user -> user.getId());
    }

    @Override
    public Mono<Long> findIdByNickname(String nickname) {
        Optional<Long> first = MAP.values().stream()
                .filter(user -> user.getNickname().equals(nickname))
                .map(user -> user.getId())
                .findFirst();

        return Mono.justOrEmpty(first);
    }

    @Override
    public Mono<User> save(User newUser) {
        User user = new User(sequence, newUser.getEmail(), newUser.getPassword(), newUser.getNickname(), null);

        MAP.put(sequence++, user);

        return Mono.just(user);
    }

    @Override
    public Mono<User> findByEmail(String email) {
        Optional<User> userOptional = MAP.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();

        return Mono.justOrEmpty(userOptional);
    }

    @Override
    public Mono<User> findById(Long userId) {
        return Mono.justOrEmpty(MAP.get(userId));
    }
}
