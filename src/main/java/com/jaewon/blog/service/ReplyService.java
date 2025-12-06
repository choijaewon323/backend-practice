package com.jaewon.blog.service;

import com.jaewon.blog.entity.report.Report;
import com.jaewon.blog.entity.reply.Reply;
import com.jaewon.blog.repository.ReplyRepository;
import com.jaewon.blog.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final UserService userService;
    private final ReportRepository reportRepository;

    public Mono<Boolean> createReply(String content, String email, Long postId) {
        return userService.getUserIdFromEmail(email)
                .flatMap(userId -> replyRepository.save(Reply.newReply(content, userId, postId)))
                .thenReturn(true)
                .onErrorReturn(false);
    }

    public Mono<Boolean> deleteByReplyId(Long replyId) {
        return replyRepository.deleteById(replyId)
                .thenReturn(true)
                .onErrorReturn(false);
    }

    public Mono<Boolean> deleteAllRepliesByUserId(Long userId) {
        return replyRepository.deleteAllByUserId(userId)
                .thenReturn(true)
                .onErrorReturn(false);
    }

    public Mono<Boolean> deleteAllRepliesByPostId(Long postId) {
        return replyRepository.deleteAllByPostId(postId)
                .thenReturn(true)
                .onErrorReturn(false);
    }

    public Mono<Void> reportReply(Long replyId, Long reporterId, String content) {
        return replyRepository.findById(replyId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("해당 댓글을 신고할 수 없습니다")))
                .flatMap(reply -> {
                    Report report = Report.newReportReply(reporterId, replyId, content);

                    return reportRepository.save(report)
                            .flatMap(savedReport -> {
                                Long reportId = savedReport.getId();

                                reply.report(reportId);

                                return replyRepository.save(reply);
                            });
                })
                .then();
    }
}
