package com.jaewon.blog.service.post;

import com.jaewon.blog.entity.post.Post;
import com.jaewon.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostUpdateService {
    private final PostRepository postRepository;

    public Mono<Void> updatePost(Long postId, String title, String content) {
        return findById(postId)
                .flatMap(post -> {
                    post.updatePost(title, content);

                    return postRepository.save(post);
                })
                .then();
    }

    public Mono<Void> updatePostReportedState(int reportedCount, long postId) {
        return findById(postId)
                .flatMap(post -> {
                    if (post.isBanned()) {
                        return Mono.empty();
                    }

                    if (post.isBannedLimit(reportedCount)) {
                        post.ban();
                    }

                    return postRepository.save(post);
                })
                .then();
    }

    private Mono<Post> findById(long postId) {
        return postRepository.findById(postId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("해당하는 게시글이 없습니다")));
    }
}
