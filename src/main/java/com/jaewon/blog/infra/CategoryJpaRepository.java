package com.jaewon.blog.infra;

import com.jaewon.blog.entity.Category;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CategoryJpaRepository extends ReactiveCrudRepository<Category, Long> {
    Mono<Long> findIdByName(String name);
}
