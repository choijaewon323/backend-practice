package com.jaewon.blog.service;

import com.jaewon.blog.entity.report.Report;
import com.jaewon.blog.entity.report.ReportType;
import com.jaewon.blog.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReportService {
    private final ReportRepository reportRepository;

    public Mono<Void> saveReportPost(Long postId, Long reporterId, String content) {
        return reportRepository.save(Report.newReportPost(postId, reporterId, content))
                .then();
    }

    public Mono<Integer> getCountPostReported(Long postId) {
        return reportRepository.findAllByTargetIdAndType(postId, ReportType.POST)
                .collectList()
                .map(List::size);
    }
}
