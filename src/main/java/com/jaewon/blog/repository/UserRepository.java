package com.jaewon.blog.repository;

import com.jaewon.blog.entity.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<Long> findIdByEmail(String email);

    Mono<Long> findIdByNickname(String nickname);

    Mono<User> save(User newUser);

    Mono<User> findByEmail(String email);

    Mono<User> findById(Long userId);

    Mono<Void> deleteById(Long userId);
}
