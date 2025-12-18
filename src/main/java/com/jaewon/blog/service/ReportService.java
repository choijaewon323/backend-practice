package com.jaewon.blog.service;

import com.jaewon.blog.entity.report.Report;
import com.jaewon.blog.entity.report.ReportType;
import com.jaewon.blog.repository.ReportRepository;
import com.jaewon.blog.service.post.PostUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final ReplyService replyService;
    private final PostUpdateService postUpdateService;

    public Mono<Void> reportReply(Long replyId, Long reporterId, String content) {
        return reportRepository.save(Report.newReportReply(reporterId, replyId, content))
                .then(getCountReplyReported(replyId))
                .flatMap(reportedCount -> replyService.updateReportedState(reportedCount, replyId))
                .then();
    }

    public Mono<Void> reportPost(long postId, long reporterId, String content) {
        return reportRepository.save(Report.newReportPost(postId, reporterId, content))
                .then(getCountPostReported(postId))
                .flatMap(reportedCount -> postUpdateService.updatePostReportedState(reportedCount, postId))
                .then();
    }

    private Mono<Integer> getCountPostReported(Long postId) {
        return reportRepository.findAllByTargetIdAndType(postId, ReportType.POST)
                .collectList()
                .map(List::size);
    }

    private Mono<Integer> getCountReplyReported(long replyId) {
        return reportRepository.findAllByTargetIdAndType(replyId, ReportType.REPLY)
                .collectList()
                .map(List::size);
    }
}
