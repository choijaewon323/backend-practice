package com.jaewon.blog.entity.like;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {
    @Id
    private Long id;

    private Long userId;
    private Long targetId;
    private LikeType type;
    private Boolean isLiked;

    @CreatedDate
    private LocalDateTime createdAt;

    public static Like newPostLike(Long userId, Long targetId) {
        return new Like(null, userId, targetId, LikeType.POST, true, null);
    }

    public static Like newReplyLike(Long userId, Long targetId) {
        return new Like(null, userId, targetId, LikeType.REPLY, true, null);
    }

    public void like() {
        isLiked = true;
    }

    public void dislike() {
        isLiked = false;
    }
}
