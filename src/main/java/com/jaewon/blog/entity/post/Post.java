package com.jaewon.blog.entity.post;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table("posts")
public class Post {
    @Id
    private Long id;

    private String title;
    private String content;
    private Long userId;
    private Long categoryId;
    private PostState state;

    @CreatedDate
    private LocalDateTime createdAt;

    private Post(Long id, String title, String content, Long userId, Long categoryId, LocalDateTime createdAt) {
        checkTitleUnder50(title);

        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.categoryId = categoryId;
        this.state = PostState.NORMAL;
        this.createdAt = createdAt;
    }

    public static Post newPost(String title,
                               String content,
                               Long userId,
                               Long categoryId) {
        return new Post(null, title, content, userId, categoryId, null);
    }

    public void banPost() {
        this.state = PostState.BAN;
    }

    public boolean isBanned() {
        return this.state == PostState.BAN;
    }

    public void updatePost(String title, String content) {
        checkTitleUnder50(title);

        this.title = title;
        this.content = content;
    }

    private void checkTitleUnder50(String title) {
        if (title.length() > 50) {
            throw new IllegalArgumentException("게시글 제목은 50글자를 넘을 수 없습니다");
        }
    }
}
