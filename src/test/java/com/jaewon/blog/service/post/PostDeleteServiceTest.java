package com.jaewon.blog.service.post;

import com.jaewon.blog.fake.FakePostRepository;
import com.jaewon.blog.fake.FakeReplyRepository;
import com.jaewon.blog.repository.PostRepository;
import com.jaewon.blog.service.ReplyService;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class PostDeleteServiceTest {
    private final PostRepository postRepository = new FakePostRepository();
    private final FakeReplyRepository fakeReplyRepository = new FakeReplyRepository();

    private final ReplyService replyService = new ReplyService(
            fakeReplyRepository
    );

    private final PostDeleteService postDeleteService = new PostDeleteService(
            postRepository,
            replyService
    );

    @Test
    void deletePostAndReplies_댓글삭제성공시Empty리턴() {
        Mono<Void> result = postDeleteService.deletePostAndReplies(0L);

        StepVerifier.create(result)
                .expectComplete()
                .verify();
    }

    @Test
    void deleteAllPostAndRepliesByUserId_댓글삭제성공시empty리턴() {
        Mono<Void> result = postDeleteService.deleteAllPostAndRepliesByUserId(0L);

        StepVerifier.create(result)
                .expectComplete()
                .verify();
    }
}
