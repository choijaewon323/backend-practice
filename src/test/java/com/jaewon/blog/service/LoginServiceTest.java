package com.jaewon.blog.service;

import com.jaewon.blog.entity.User;
import com.jaewon.blog.fake.FakeUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

class LoginServiceTest {
    private final FakeUserRepository fakeUserRepository = new FakeUserRepository();
    private final LoginService loginService = new LoginService(fakeUserRepository);

    @AfterEach
    void after() {
        FakeUserRepository.MAP.clear();
    }

    @Test
    void test() {
        Mono<Boolean> loginMono = loginService.login("email", "password");

        StepVerifier.create(loginMono)
                .assertNext(res -> {
                    assertThat(res).isFalse();
                })
                .verifyComplete();
    }

    @Test
    void test2() {
        Mono<Boolean> result = fakeUserRepository.save(User.newUser("email", "password", "nickname"))
                .then(loginService.login("email", "password1"));

        StepVerifier.create(result)
                .assertNext(res -> {
                    assertThat(res).isFalse();
                })
                .verifyComplete();
    }

    @Test
    void test3() {
        Mono<Boolean> result = fakeUserRepository.save(User.newUser("email", "password", "nickname"))
                .then(loginService.login("email", "password"));

        StepVerifier.create(result)
                .assertNext(res -> {
                    assertThat(res).isTrue();
                })
                .verifyComplete();
    }
}
