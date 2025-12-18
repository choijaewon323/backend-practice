package com.jaewon.blog.fake;

import com.jaewon.blog.entity.Category;
import com.jaewon.blog.repository.CategoryRepository;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class FakeCategoryRepository implements CategoryRepository {
    public final Map<Long, Category> map = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0L);

    @Override
    public Mono<Long> findIdByName(String name) {
        return Mono.fromCallable(() -> map.values().stream()
                .filter(category -> category.getName().equals(name))
                .map(Category::getId)
                .findFirst())
                .flatMap(Mono::justOrEmpty);
    }

    @Override
    public Mono<Category> findById(Long categoryId) {
        return Mono.fromCallable(() -> Optional.ofNullable(map.get(categoryId)))
                .flatMap(Mono::justOrEmpty);
    }

    @Override
    public Mono<Category> save(Category category) {
        return Mono.fromCallable(() -> {
            if (category.getId() != null && map.containsKey(category.getId())) {
                map.put(category.getId(), category);
                return category;
            }

            long id = sequence.getAndIncrement();
            Category newCategory = new Category(id, category.getName(), null);
            map.put(id, newCategory);

            return newCategory;
        });
    }

    public Mono<Void> deleteAll() {
        return Mono.fromRunnable(map::clear);
    }
}
