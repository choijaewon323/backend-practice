package com.jaewon.blog.service.user;

import com.jaewon.blog.entity.User;
import com.jaewon.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Mono<Boolean> createUser(String email, String password, String nickname) {
        return userRepository.findIdByEmail(email)
                .flatMap(userId -> Mono.error(new IllegalArgumentException("이미 존재하는 이메일입니다")))
                .switchIfEmpty(userRepository.findIdByNickname(nickname))
                .flatMap(userId -> Mono.error(new IllegalArgumentException("이미 존재하는 닉네입입니다")))
                .switchIfEmpty(Mono.defer(() -> {
                    User newUser = User.newUser(email, password, nickname);

                    return userRepository.save(newUser);
                }))
                .thenReturn(true)
                .onErrorReturn(false);
    }

    public Mono<Boolean> updatePassword(String email, String oldPassword, String newPassword) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("해당 유저가 없습니다")))
                .flatMap(user -> {
                    if (!user.isPasswordSame(oldPassword)) {
                        return Mono.error(new IllegalArgumentException("비밀번호가 다릅니다"));
                    }

                    if (oldPassword.equals(newPassword)) {
                        return Mono.error(new IllegalArgumentException("비밀번호가 동일합니다"));
                    }

                    user.updatePassword(newPassword);

                    return userRepository.save(user);
                })
                .thenReturn(true)
                .onErrorReturn(false);
    }
}
