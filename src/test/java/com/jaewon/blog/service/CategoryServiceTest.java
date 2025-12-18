package com.jaewon.blog.service;

import com.jaewon.blog.entity.Category;
import com.jaewon.blog.fake.FakeCategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryServiceTest {
    private final FakeCategoryRepository fakeCategoryRepository = new FakeCategoryRepository();
    private final CategoryService categoryService = new CategoryService(fakeCategoryRepository);

    @AfterEach
    void after() {
        fakeCategoryRepository.deleteAll()
                .block();
    }

    @Test
    void findCategoryIdFromName_성공_테스트() {
        Mono<Long> result = fakeCategoryRepository.save(new Category(0L, "test name", null))
                .then(categoryService.findCategoryIdFromName("test name"));

        StepVerifier.create(result)
                .assertNext(categoryId -> {
                    assertThat(categoryId).isZero();
                })
                .verifyComplete();
    }

    @Test
    void getCategoryNameFromId_성공테스트() {
        Mono<String> result = Mono.fromRunnable(() -> fakeCategoryRepository.map.put(0L, new Category(0L, "test name", null)))
                .then(categoryService.getCategoryNameFromId(0L));

        StepVerifier.create(result)
                .assertNext(name -> {
                    assertThat(name).isEqualTo("test name");
                })
                .verifyComplete();
    }

    @Test
    void createCategory_중복_이름_생성시_에러() {
        Mono<Void> result = fakeCategoryRepository.save(Category.newCategory("name"))
                .then(categoryService.createCategory("name"));

        StepVerifier.create(result)
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(IllegalArgumentException.class);
                })
                .verify();
    }

    @Test
    void createCategory_성공_테스트() {
        Mono<Void> result = fakeCategoryRepository.save(Category.newCategory("name"))
                .then(categoryService.createCategory("newName"));

        StepVerifier.create(result)
                .verifyComplete();

        Optional<Category> newName = fakeCategoryRepository.map.values().stream()
                .filter(category -> category.getName().equals("newName"))
                .findFirst();

        assertThat(newName).isPresent();
    }
}
