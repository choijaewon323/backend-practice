package com.jaewon.blog.repository;

import com.jaewon.blog.entity.Tag;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface TagRepository extends ReactiveCrudRepository<Tag, Long> {
    Mono<Long> findIdByName(String name);
}
