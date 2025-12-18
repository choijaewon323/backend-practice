package com.jaewon.blog.service.post;

import com.jaewon.blog.entity.Category;
import com.jaewon.blog.entity.User;
import com.jaewon.blog.entity.post.Post;
import com.jaewon.blog.fake.FakeCategoryRepository;
import com.jaewon.blog.fake.FakePostRepository;
import com.jaewon.blog.fake.FakeUserRepository;
import com.jaewon.blog.service.CategoryService;
import com.jaewon.blog.service.user.UserQueryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PostCreateServiceTest {
    private final FakePostRepository fakePostRepository = new FakePostRepository();
    private final FakeCategoryRepository fakeCategoryRepository = new FakeCategoryRepository();
    private final FakeUserRepository fakeUserRepository = new FakeUserRepository();
    private final UserQueryService userQueryService = new UserQueryService(fakeUserRepository);
    private final CategoryService categoryService = new CategoryService(fakeCategoryRepository);

    private final PostCreateService postCreateService = new PostCreateService(
            fakePostRepository,
            userQueryService,
            categoryService
    );

    @AfterEach
    void tearDown() {
        Mono.when(
                fakeCategoryRepository.deleteAll(),
                Mono.fromRunnable(fakeUserRepository.map::clear),
                fakePostRepository.deleteAll()
        ).block();
    }

    @Test
    void createPost_생성성공테스트() {
        String title = "테스트 제목";
        String content = "테스트 내용";
        String email = "test@email.com";
        String categoryName = "testCategory";

        fakeUserRepository.save(User.newUser(email, "asdf", "asdf"))
                .then(fakeCategoryRepository.save(Category.newCategory(categoryName)))
                .block();

        Mono<Void> result = postCreateService.createPost(title, content, email, categoryName);

        StepVerifier.create(result)
                .then(() -> {
                    Optional<Post> saved = fakePostRepository.map.values()
                            .stream()
                            .findFirst();

                    Optional<User> userSaved = fakeUserRepository.map.values()
                            .stream()
                            .findFirst();

                    Optional<Category> categorySaved = fakeCategoryRepository.map.values()
                            .stream()
                            .findFirst();

                    assertThat(saved).isPresent();
                    assertThat(saved.get().getTitle()).isEqualTo(title);
                    assertThat(saved.get().getContent()).isEqualTo(content);
                    assertThat(userSaved).isPresent();
                    assertThat(categorySaved).isPresent();
                    assertThat(userSaved.get().getId()).isEqualTo(saved.get().getUserId());
                    assertThat(categorySaved.get().getId()).isEqualTo(saved.get().getCategoryId());
                })
                .verifyComplete();
    }

    @Test
    void createPost_이메일에_해당하는_user가없거나카테고리가없으면_예외() {
        String title = "테스트 제목";
        String content = "테스트 내용";
        String email = "test@email.com";
        String categoryName = "testCategory";

        Mono<Void> result = postCreateService.createPost(title, content, email, categoryName);

        StepVerifier.create(result)
                .expectError()
                .verify();
    }
}
