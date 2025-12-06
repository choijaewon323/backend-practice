package com.jaewon.blog.service;

import com.jaewon.blog.infra.UserJpaRepository;
import com.jaewon.blog.service.post.PostDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserDeleteService {
    private final PostDeleteService postDeleteService;
    private final UserJpaRepository userRepository;
    private final LikeService likeService;

    public Mono<Boolean> deleteUserAndBoardsAndReplies(String email) {
        return userRepository.findIdByEmail(email)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("유저 삭제에 실패했습니다. 해당 이메일의 유저가 없습니다.")))
                .flatMap(userId -> Mono.zip(
                        userRepository.deleteById(userId),
                        postDeleteService.deleteAllPostAndRepliesByUserId(userId),
                        likeService.deleteLikesByUserId(userId)
                                .thenReturn(true)
                ))
                .flatMap(tuple -> {
                    boolean isBoardDeletionSuccess = tuple.getT2();

                    if (!isBoardDeletionSuccess) {
                        return Mono.error(new IllegalArgumentException("해당 유저의 게시글 삭제에 실패했습니다."));
                    }

                    return Mono.just(true);
                })
                .onErrorReturn(false);
    }
}
