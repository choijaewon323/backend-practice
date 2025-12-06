package com.jaewon.blog.controller;

import com.jaewon.blog.common.CommonResponse;
import com.jaewon.blog.service.post.PostDeleteService;
import com.jaewon.blog.service.post.PostService;
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
    private final PostService postService;
    private final PostDeleteService postDeleteService;

    @PostMapping
    public Mono<CommonResponse<Boolean>> createPost(@RequestBody CreatePostRequest request) {
        return postService.createPost(request.title, request.content, request.email, request.categoryName)
                .doFirst(request::validate)
                .map(CommonResponse::success);
    }

    @PutMapping("/{postId}")
    public Mono<CommonResponse<Boolean>> updatePost(@PathVariable Long postId, @RequestBody UpdatePostRequest request) {
        return postService.updatePost(postId, request.title, request.content)
                .doFirst(() -> {
                    Objects.requireNonNull(postId, "postId가 없습니다");
                    request.validate();
                })
                .map(CommonResponse::success);
    }

    @DeleteMapping("/{postId}")
    public Mono<CommonResponse<Boolean>> deletePost(@PathVariable Long postId) {
        return postDeleteService.deletePostAndReplies(postId)
                .doFirst(() -> Objects.requireNonNull(postId, "postId"))
                .map(CommonResponse::success);
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
