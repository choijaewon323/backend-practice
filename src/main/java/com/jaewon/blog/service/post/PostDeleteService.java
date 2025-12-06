package com.jaewon.blog.service.post;

import com.jaewon.blog.repository.PostRepository;
import com.jaewon.blog.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PostDeleteService {
    private final PostRepository postRepository;
    private final ReplyService replyService;

    public Mono<Boolean> deletePostAndReplies(Long postId) {
        return postRepository.deleteById(postId)
                .then(replyService.deleteAllRepliesByPostId(postId))
                .flatMap(isReplyDeletionSuccess -> {
                    if (!isReplyDeletionSuccess) {
                        return Mono.error(new IllegalStateException("게시글 삭제 중 오류가 발생했습니다"));
                    }

                    return Mono.just(true);
                })
                .onErrorReturn(false);
    }

    public Mono<Boolean> deleteAllPostAndRepliesByUserId(Long userId) {
        return postRepository.deleteAllByUserId(userId)
                .then(replyService.deleteAllRepliesByUserId(userId))
                .thenReturn(true)
                .onErrorReturn(false);
    }
}
