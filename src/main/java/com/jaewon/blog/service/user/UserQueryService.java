package com.jaewon.blog.service.user;

import com.jaewon.blog.entity.User;
import com.jaewon.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class UserQueryService {
    private final UserRepository userRepository;

    public Mono<Long> getUserIdFromEmail(String email) {
        return userRepository.findIdByEmail(email)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("해당 닉네임의 유저가 없습니다.")));
    }

    public Mono<Long> findIdByEmail(String email) {
        return userRepository.findIdByEmail(email)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("해당 닉네임의 유저가 없습니다.")));
    }

    public Mono<Long> findIdByNickname(String nickname) {
        return userRepository.findIdByNickname(nickname);
    }

    public Mono<String> getNicknameById(long userId) {
        return userRepository.findById(userId)
                .map(User::getNickname);
    }
}
