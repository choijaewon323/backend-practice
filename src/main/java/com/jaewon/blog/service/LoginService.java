package com.jaewon.blog.service;

import com.jaewon.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class LoginService {
    private final UserRepository userRepository;

    public Mono<Boolean> login(String email, String password) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("해당 이메일의 유저가 없습니다")))
                .flatMap(user -> {
                    if (!user.isPasswordSame(password)) {
                        return Mono.error(new IllegalArgumentException("비밀번호가 다릅니다"));
                    }

                    return Mono.just(true);
                })
                .onErrorReturn(false);
    }
}
