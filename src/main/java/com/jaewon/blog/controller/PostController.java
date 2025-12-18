package com.jaewon.blog.controller;

import com.jaewon.blog.common.CommonResponse;
import com.jaewon.blog.service.post.PostCreateService;
import com.jaewon.blog.service.post.PostDeleteService;
import com.jaewon.blog.service.post.PostUpdateService;
import com.jaewon.blog.util.StringValidationUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Objects;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostUpdateService postUpdateService;
    private final PostDeleteService postDeleteService;
    private final PostCreateService postCreateService;

    @PostMapping
    public Mono<CommonResponse<Boolean>> createPost(@RequestBody CreatePostRequest request) {
        return postCreateService.createPost(request.title, request.content, request.email, request.categoryName)
                .doFirst(request::validate)
                .thenReturn(CommonResponse.success(true));
    }

    @PutMapping("/{postId}")
    public Mono<CommonResponse<Boolean>> updatePost(@PathVariable Long postId, @RequestBody UpdatePostRequest request) {
        return postUpdateService.updatePost(postId, request.title, request.content)
                .doFirst(() -> {
                    Objects.requireNonNull(postId, "postId가 없습니다");
                    request.validate();
                })
                .thenReturn(true)
                .map(CommonResponse::success)
                .onErrorReturn(CommonResponse.fail(false));
    }

    @DeleteMapping("/{postId}")
    public Mono<CommonResponse<Boolean>> deletePost(@PathVariable Long postId) {
        return postDeleteService.deletePostAndReplies(postId)
                .doFirst(() -> Objects.requireNonNull(postId, "postId"))
                .thenReturn(CommonResponse.success(true));
    }

    @Getter
    @NoArgsConstructor
    public static class UpdatePostRequest {
        private String title;
        private String content;

        public void validate() {
            StringValidationUtil.checkStringEmpty(title, "title");
            StringValidationUtil.checkStringEmpty(content, "content");
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CreatePostRequest {
        private String title;
        private String content;
        private String email;
        private String categoryName;

        public void validate() {
            StringValidationUtil.checkStringEmpty(title, "title");
            StringValidationUtil.checkStringEmpty(content, "content");
            StringValidationUtil.checkStringEmpty(email, "email");
            StringValidationUtil.checkStringEmpty(categoryName, "categoryName");
        }
    }
}
