package com.jaewon.blog.service;

import com.jaewon.blog.entity.Category;
import com.jaewon.blog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Mono<Void> createCategory(String name) {
        return categoryRepository.findIdByName(name)
                .flatMap(id -> Mono.error(new IllegalArgumentException("이미 있는 카테고리입니다")))
                .switchIfEmpty(Mono.defer(() -> {
                    Category category = Category.newCategory(name);

                    return categoryRepository.save(category);
                }))
                .then();
    }

    public Mono<Long> findCategoryIdFromName(String name) {
        return categoryRepository.findIdByName(name)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("해당 카테고리가 없습니다.")));
    }

    public Mono<String> getCategoryNameFromId(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(Category::getName);
    }
}
