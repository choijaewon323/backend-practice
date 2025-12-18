package com.jaewon.blog.service.post;

import com.jaewon.blog.repository.PostRepository;
import com.jaewon.blog.service.CategoryService;
import com.jaewon.blog.service.PostTagMappingService;
import com.jaewon.blog.service.TagService;
import com.jaewon.blog.service.user.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostQueryService {
    private final PostRepository postRepository;
    private final TagService tagService;
    private final PostTagMappingService postTagMappingService;
    private final CategoryService categoryService;
    private final UserQueryService userQueryService;

    public Mono<PostDetailResponse> getPostDetail(Long postId) {
        return postRepository.findById(postId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("해당 게시글이 없습니다")))
                .flatMap(post -> Mono.zip(
                        postTagMappingService.getTagIdsByPostId(postId)
                                .collectList()
                                .flatMap(tagIds -> tagService.getTagNamesFromTagId(tagIds)
                                        .collectList()),
                        userQueryService.getNicknameById(post.getUserId()),
                        categoryService.getCategoryNameFromId(post.getCategoryId())
                ).map(tuple -> {
                    List<String> tagNames = tuple.getT1();
                    String nickname = tuple.getT2();
                    String categoryName = tuple.getT3();

                    return new PostDetailResponse(
                            post.getId(),
                            post.getTitle(),
                            post.getContent(),
                            nickname,
                            tagNames,
                            categoryName,
                            post.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME)
                    );
                }));
    }

    @Getter
    @AllArgsConstructor
    public static class PostDetailResponse {
        private final Long id;
        private final String title;
        private final String content;
        private final String nickname;
        private final List<String> tags;
        private final String categoryName;
        private final String createdAt;
    }
}
