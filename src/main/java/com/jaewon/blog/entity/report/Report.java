package com.jaewon.blog.entity.report;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Report {
    @Id
    private Long id;

    private Long targetId;
    private Long reporterId;
    private String content;
    private ReportType type;

    @CreatedDate
    private LocalDateTime createdAt;

    public Report(Long id, Long targetId, Long reporterId, String content, ReportType type, LocalDateTime createdAt) {
        this.id = id;
        this.targetId = targetId;
        this.reporterId = reporterId;
        this.content = content;
        this.createdAt = createdAt;
        this.type = type;
    }

    public static Report newReportPost(Long reporterId, Long postId, String content) {
        return new Report(null, postId, reporterId, content, ReportType.POST, null);
    }

    public static Report newReportReply(Long reporterId, Long replyId, String content) {
        return new Report(null, replyId, reporterId, content, ReportType.REPLY, null);
    }
}
