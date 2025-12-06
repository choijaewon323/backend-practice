package com.jaewon.blog.infra;

import com.jaewon.blog.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserJpaRepository extends ReactiveCrudRepository<User, Long> {
    Mono<Long> findIdByEmail(String email);
    Mono<User> findByEmail(String email);
    Mono<Long> findIdByNickname(String nickname);
}
