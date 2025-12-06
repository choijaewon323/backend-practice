package com.jaewon.blog.fake;

import com.jaewon.blog.entity.Category;
import com.jaewon.blog.repository.CategoryRepository;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeCategoryRepository implements CategoryRepository {
    public static Map<Long, Category> MAP = new HashMap<>();
    public static long SEQUENCE = 0L;

    @Override
    public Mono<Long> findIdByName(String name) {
        Optional<Long> id = MAP.values().stream()
                .filter(category -> category.getName().equals(name))
                .map(Category::getId)
                .findFirst();

        return Mono.justOrEmpty(id);
    }

    @Override
    public Mono<Category> findById(Long categoryId) {
        return Mono.justOrEmpty(MAP.get(categoryId));
    }

    @Override
    public Mono<Category> save(Category category) {
        Category newCategory = new Category(SEQUENCE, category.getName(), null);

        MAP.put(SEQUENCE++, newCategory);

        return Mono.just(newCategory);
    }
}
