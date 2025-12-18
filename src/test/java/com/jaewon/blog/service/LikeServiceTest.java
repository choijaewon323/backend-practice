package com.jaewon.blog.service;

import com.jaewon.blog.entity.like.Like;
import com.jaewon.blog.fake.FakeLikeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class LikeServiceTest {
    private final FakeLikeRepository fakeLikeRepository = new FakeLikeRepository();
    private final LikeService likeService = new LikeService(fakeLikeRepository);

    @AfterEach
    void tearDown() {
        fakeLikeRepository.map.clear();
    }

    @Test
    void 게시글_좋아요_시_없으면_추가() {
        long userId = 0L;
        long postId = 0L;

        Mono<Void> voidMono = likeService.likePost(userId, postId);

        StepVerifier.create(voidMono)
                .then(() -> {
                    Optional<Like> first = fakeLikeRepository.map.values().stream()
                            .filter(like -> like.getTargetId().equals(0L) && like.getUserId().equals(0L))
                            .findFirst();

                    assertThat(first).isPresent();
                    assertThat(first.get().getIsLiked()).isTrue();
                })
                .verifyComplete();
    }

    @Test
    void 게시글_좋아요_시_이미_있으면_isLiked_토글링() {
        Mono<Void> then = Mono.fromRunnable(() -> {
                    Like postLike = Like.newPostLike(0L, 0L);

                    postLike.dislike();

                    fakeLikeRepository.save(postLike);
                })
                .then(likeService.likePost(0L, 0L));

        StepVerifier.create(then)
                .then(() -> {
                    Optional<Like> first = fakeLikeRepository.map.values().stream()
                            .filter(like -> like.getTargetId().equals(0L) && like.getUserId().equals(0L))
                            .findFirst();

                    assertThat(first).isPresent();
                    assertThat(first.get().getIsLiked()).isTrue();
                })
                .verifyComplete();
    }

    @Test
    void 게시글_좋아요_취소_시_좋아요_없으면_에러() {
        long userId = 0L;
        long postId = 0L;

        Mono<Void> voidMono = likeService.dislikePost(userId, postId);

        StepVerifier.create(voidMono)
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(IllegalArgumentException.class);
                    assertThat(error.getMessage()).isEqualTo("해당 게시글 좋아요 취소에 실패했습니다");
                })
                .verify();
    }
}
