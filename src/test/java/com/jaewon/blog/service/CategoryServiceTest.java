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
        FakeCategoryRepository.MAP.clear();
    }

    @Test
    void test() {
        // given
        FakeCategoryRepository.MAP.put(0L, new Category(0L, "test name", null));

        // when
        Mono<Long> id = categoryService.findCategoryIdFromName("test name");

        // then
        StepVerifier.create(id)
                .assertNext(categoryId -> {
                    assertThat(categoryId).isZero();
                })
                .verifyComplete();
    }

    @Test
    void test2() {
        FakeCategoryRepository.MAP.put(0L, new Category(0L, "test name", null));

        Mono<String> categoryName = categoryService.getCategoryNameFromId(0L);

        StepVerifier.create(categoryName)
                .assertNext(name -> {
                    assertThat(name).isEqualTo("test name");
                })
                .verifyComplete();
    }

    @Test
    void test3() {
        Mono<Void> result = fakeCategoryRepository.save(Category.newCategory("name"))
                .then(categoryService.createCategory("name"));

        StepVerifier.create(result)
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(IllegalArgumentException.class);
                })
                .verify();
    }

    @Test
    void test4() {
        Mono<Void> result = fakeCategoryRepository.save(Category.newCategory("name"))
                .then(categoryService.createCategory("newName"));

        StepVerifier.create(result)
                .verifyComplete();

        Optional<Category> newName = FakeCategoryRepository.MAP.values().stream()
                .filter(category -> category.getName().equals("newName"))
                .findFirst();

        assertThat(newName).isPresent();
    }
}
