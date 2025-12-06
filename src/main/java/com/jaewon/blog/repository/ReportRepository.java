package com.jaewon.blog.repository;

import com.jaewon.blog.entity.report.Report;
import com.jaewon.blog.entity.report.ReportType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReportRepository {
    Mono<Report> save(Report report);
    Flux<Report> findAllByTargetIdAndType(Long targetId, ReportType type);
}
