package com.jaewon.blog.service;

import com.jaewon.blog.entity.User;
import com.jaewon.blog.fake.FakeUserRepository;
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
        FakeUserRepository.MAP.clear();
    }

    @Test
    void test() {
        fakeUserRepository.save(User.newUser("test email", "test password", "test nickname"));

        Mono<Boolean> result = userService.createUser("test email", "password", "nickname");

        StepVerifier.create(result)
                .assertNext(res -> {
                    assertThat(res).isFalse();
                })
                .verifyComplete();
    }

    @Test
    void test2() {
        Mono<Boolean> then = fakeUserRepository.save(User.newUser("test email", "test password", "test nickname"))
                .then(userService.createUser("email", "password", "test nickname"));

        StepVerifier.create(then)
                .assertNext(res -> {
                    assertThat(res).isFalse();
                })
                .verifyComplete();
    }

    @Test
    void test3() {
        fakeUserRepository.save(User.newUser("test email", "test password", "test nickname"));

        Mono<Boolean> result = userService.createUser("email", "password", "nickname");

        StepVerifier.create(result)
                .assertNext(res -> {
                    assertThat(res).isTrue();
                })
                .verifyComplete();
    }

    @Test
    void test4() {
        fakeUserRepository.save(User.newUser("test email", "test password", "test nickname"));

        Mono<Boolean> booleanMono = userService.updatePassword("email", "test password", "test nickname");

        StepVerifier.create(booleanMono)
                .assertNext(res -> {
                    assertThat(res).isFalse();
                })
                .verifyComplete();
    }

    @Test
    void test5() {
        fakeUserRepository.save(User.newUser("test email", "test password", "test nickname"));

        Mono<Boolean> booleanMono = userService.updatePassword("test email", "password", "test password");

        StepVerifier.create(booleanMono)
                .assertNext(res -> {
                    assertThat(res).isFalse();
                })
                .verifyComplete();
    }

    @Test
    void test6() {
        fakeUserRepository.save(User.newUser("test email", "test password", "test nickname"));

        Mono<Boolean> booleanMono = userService.updatePassword("test email", "test password", "test password");

        StepVerifier.create(booleanMono)
                .assertNext(res -> {
                    assertThat(res).isFalse();
                })
                .verifyComplete();
    }

    @Test
    void test7() {
        fakeUserRepository.save(User.newUser("test email", "test password", "test nickname"));

        Mono<Boolean> booleanMono = userService.updatePassword("test email", "test password", "asdf");

        StepVerifier.create(booleanMono)
                .assertNext(res -> {
                    assertThat(res).isTrue();
                })
                .verifyComplete();
    }

    @Test
    void test8() {
        Mono<Long> result = fakeUserRepository.save(User.newUser("email", "password", "nickname"))
                .then(userService.getUserIdFromEmail("emailasd"));

        StepVerifier.create(result)
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(IllegalArgumentException.class);
                    assertThat(error.getMessage()).isEqualTo("해당 닉네임의 유저가 없습니다.");
                })
                .verify();
    }
}
