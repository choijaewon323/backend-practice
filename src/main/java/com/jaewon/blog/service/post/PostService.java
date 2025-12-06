package com.jaewon.blog.service.post;

import com.jaewon.blog.entity.post.Post;
import com.jaewon.blog.repository.PostRepository;
import com.jaewon.blog.service.CategoryService;
import com.jaewon.blog.service.ReportService;
import com.jaewon.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ReportService reportService;

    public Mono<Boolean> createPost(String title, String content, String email, String categoryName) {
        return Mono.zip(userService.getUserIdFromEmail(email), categoryService.findCategoryIdFromName(categoryName))
                        .flatMap(tuple -> {
                            Long userId = tuple.getT1();
                            Long categoryId = tuple.getT2();

                            return postRepository.save(Post.newPost(title, content, userId, categoryId));
                        })
                .thenReturn(true)
                .doOnError(error -> log.error("에러가 발생했습니다 : ", error))
                .onErrorReturn(false);
    }

    public Mono<Boolean> updatePost(Long postId, String title, String content) {
        return postRepository.findById(postId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("해당하는 게시글이 없습니다")))
                .flatMap(post -> {
                    post.updatePost(title, content);

                    return postRepository.save(post);
                })
                .thenReturn(true)
                .onErrorReturn(false);
    }

    public Mono<Void> reportPost(Long postId, Long reporterId, String content) {
        return postRepository.findById(postId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("해당 게시글이 없습니다")))
                .flatMap(post -> {
                    if (post.isBanned()) {
                        return Mono.empty();
                    }

                    return reportService.saveReportPost(postId, reporterId, content)
                            .then(reportService.getCountPostReported(postId))
                            .flatMap(reportedCount -> {
                                if (reportedCount > 500) {
                                    post.banPost();
                                }

                                return postRepository.save(post);
                            });
                })
                .then();
    }
}
