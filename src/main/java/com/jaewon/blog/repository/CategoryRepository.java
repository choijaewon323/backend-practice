package com.jaewon.blog.repository;

import com.jaewon.blog.entity.Category;
import reactor.core.publisher.Mono;

public interface CategoryRepository {
    Mono<Long> findIdByName(String name);

    Mono<Category> findById(Long categoryId);

    Mono<Category> save(Category category);
}
