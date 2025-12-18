package com.jaewon.blog.service;

import com.jaewon.blog.entity.User;
import com.jaewon.blog.fake.FakeUserRepository;
import com.jaewon.blog.service.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest {
    private final FakeUserRepository fakeUserRepository = new FakeUserRepository();
    private final UserService userService = new UserService(fakeUserRepository);

    @AfterEach
    void after() {
        fakeUserRepository.map.clear();
    }

    @Test
    void createUser_이미존재하는이메일이면false() {
        Mono<Boolean> result = fakeUserRepository.save(User.newUser("test email", "test password", "test nickname"))
                .then(userService.createUser("test email", "password", "nickname"));

        StepVerifier.create(result)
                .assertNext(res -> {
                    assertThat(res).isFalse();
                })
                .verifyComplete();
    }

    @Test
    void createUser_이미존재하는닉네임이면false() {
        Mono<Boolean> then = fakeUserRepository.save(User.newUser("test email", "test password", "test nickname"))
                .then(userService.createUser("email", "password", "test nickname"));

        StepVerifier.create(then)
                .assertNext(res -> {
                    assertThat(res).isFalse();
                })
                .verifyComplete();
    }

    @Test
    void createUser_성공테스트() {
        Mono<Boolean> result = fakeUserRepository.save(User.newUser("test email", "test password", "test nickname"))
                .then(userService.createUser("email", "password", "nickname"));

        StepVerifier.create(result)
                .assertNext(res -> {
                    assertThat(res).isTrue();
                })
                .verifyComplete();
    }

    @Test
    void updatePassword_존재하는이메일이없으면false() {
        Mono<Boolean> booleanMono = fakeUserRepository.save(User.newUser("test email", "test password", "test nickname"))
                .then(userService.updatePassword("email", "test password", "test nickname"));

        StepVerifier.create(booleanMono)
                .assertNext(res -> {
                    assertThat(res).isFalse();
                })
                .verifyComplete();
    }

    @Test
    void updatePassword_기존패스워드와다르면false() {
        Mono<Boolean> booleanMono = fakeUserRepository.save(User.newUser("test email", "test password", "test nickname"))
                .then(userService.updatePassword("test email", "password", "test password"));

        StepVerifier.create(booleanMono)
                .assertNext(res -> {
                    assertThat(res).isFalse();
                })
                .verifyComplete();
    }

    @Test
    void updatePassword_기존패스워드와바꿀패스워드가같으면false() {
        Mono<Boolean> booleanMono = fakeUserRepository.save(User.newUser("test email", "test password", "test nickname"))
                .then(userService.updatePassword("test email", "test password", "test password"));

        StepVerifier.create(booleanMono)
                .assertNext(res -> {
                    assertThat(res).isFalse();
                })
                .verifyComplete();
    }

    @Test
    void updatePassword_성공테스트() {
        Mono<Boolean> booleanMono = fakeUserRepository.save(User.newUser("test email", "test password", "test nickname"))
                .then(userService.updatePassword("test email", "test password", "asdf"));

        StepVerifier.create(booleanMono)
                .assertNext(res -> {
                    assertThat(res).isTrue();
                })
                .verifyComplete();
    }
}
