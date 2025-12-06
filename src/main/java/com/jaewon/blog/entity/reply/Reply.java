package com.jaewon.blog.entity.reply;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    public boolean isBanned() {
        return state == ReplyState.BAN;
    }

    public void report(Long reportId) {
    }
}
