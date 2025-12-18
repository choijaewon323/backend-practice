package com.jaewon.blog.entity.reply;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply {
    @Id
    private Long id;

    private String content;
    private Long userId;
    private Long postId;
    private ReplyState state;

    @CreatedDate
    private LocalDateTime createdAt;

    public static Reply newReply(String content, Long userId, Long postId) {
        return new Reply(null, content, userId, postId, ReplyState.NORMAL, null);
    }

    public boolean isBannedLimit(int reportedCount) {
        return reportedCount > 500;
    }

    public void ban() {
        this.state = ReplyState.BAN;
    }

    public boolean isBanned() {
        return state == ReplyState.BAN;
    }
}
