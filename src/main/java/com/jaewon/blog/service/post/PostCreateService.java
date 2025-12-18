package com.jaewon.blog.service.post;

import com.jaewon.blog.entity.post.Post;
import com.jaewon.blog.repository.PostRepository;
import com.jaewon.blog.service.CategoryService;
import com.jaewon.blog.service.user.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostCreateService {
    private final PostRepository postRepository;
    private final UserQueryService userQueryService;
    private final CategoryService categoryService;

    public Mono<Void> createPost(String title, String content, String email, String categoryName) {
        return Mono.zip(userQueryService.findIdByEmail(email), categoryService.findCategoryIdFromName(categoryName))
                .flatMap(tuple -> {
                    Long userId = tuple.getT1();
                    Long categoryId = tuple.getT2();

                    return postRepository.save(Post.newPost(title, content, userId, categoryId));
                })
                .then();
    }
}
