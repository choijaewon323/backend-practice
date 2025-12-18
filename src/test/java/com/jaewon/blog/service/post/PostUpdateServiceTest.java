package com.jaewon.blog.service.post;


import com.jaewon.blog.entity.post.Post;
import com.jaewon.blog.fake.FakePostRepository;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PostUpdateServiceTest {
    private final FakePostRepository fakePostRepository = new FakePostRepository();

    private final PostUpdateService postUpdateService = new PostUpdateService(fakePostRepository);

    @Test
    void 내용_업데이트_시_게시글이_없으면_에러() {
        Mono<Void> result = postUpdateService.updatePost(0L, "title", "content");

        StepVerifier.create(result)
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(IllegalArgumentException.class);
                    assertThat(error.getMessage()).isEqualTo("해당하는 게시글이 없습니다");
                })
                .verify();
    }

    @Test
    void 내용_업데이트_성공_테스트() {
        Mono<Void> result = Mono.fromCallable(() -> Post.newPost("test title", "test content", 0L, 0L))
                .flatMap(post -> fakePostRepository.save(post)
                .map(Post::getId)).flatMap(id -> postUpdateService.updatePost(id, "test title1", "test content1"));

        StepVerifier.create(result)
                .then(() -> {
                    Optional<Post> saved = fakePostRepository.map.values().stream()
                            .findFirst();

                    assertThat(fakePostRepository.map.size()).isOne();
                    assertThat(saved).isPresent();
                    assertThat(saved.get().getTitle()).isEqualTo("test title1");
                    assertThat(saved.get().getContent()).isEqualTo("test content1");
                })
                .verifyComplete();
    }

    @Test
    void 게시글_신고_게시글이_없는경우_예외() {
        Mono<Void> result = postUpdateService.updatePostReportedState(100, 0L);

        StepVerifier.create(result)
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(IllegalArgumentException.class);
                    assertThat(error.getMessage()).isEqualTo("해당하는 게시글이 없습니다");
                })
                .verify();
    }

    @Test
    void 게시글_신고_500건_초과_시_게시글_밴() {
        Mono<Void> result = Mono.fromCallable(() -> Post.newPost("test title", "test content", 0L, 0L))
                .flatMap(post -> fakePostRepository.save(post)
                        .map(Post::getId))
                .flatMap(id -> postUpdateService.updatePostReportedState(501, id));

        StepVerifier.create(result)
                .then(() -> {
                    Optional<Post> saved = fakePostRepository.map.values().stream()
                            .findFirst();

                    assertThat(fakePostRepository.map.size()).isOne();
                    assertThat(saved).isPresent();
                    assertThat(saved.get().isBanned()).isTrue();
                })
                .verifyComplete();
    }
}
