package com.jaewon.blog.service.user;

import com.jaewon.blog.repository.UserRepository;
import com.jaewon.blog.service.LikeService;
import com.jaewon.blog.service.post.PostDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserDeleteService {
    private final PostDeleteService postDeleteService;
    private final UserRepository userRepository;
    private final LikeService likeService;

    public Mono<Void> deleteUserAndBoardsAndReplies(String email) {
        return userRepository.findIdByEmail(email)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("유저 삭제에 실패했습니다. 해당 이메일의 유저가 없습니다.")))
                .flatMap(userId -> Mono.when(
                        userRepository.deleteById(userId),
                        postDeleteService.deleteAllPostAndRepliesByUserId(userId)
                                .thenReturn(true)
                                .onErrorReturn(false),
                        likeService.deleteLikesByUserId(userId)
                                .thenReturn(true)
                ));
    }
}
