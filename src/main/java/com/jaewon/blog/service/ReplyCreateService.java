package com.jaewon.blog.service;

import com.jaewon.blog.entity.reply.Reply;
import com.jaewon.blog.repository.ReplyRepository;
import com.jaewon.blog.service.user.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ReplyCreateService {
    private final ReplyRepository replyRepository;
    private final UserQueryService userQueryService;

    public Mono<Void> createReply(String content, String email, Long postId) {
        return userQueryService.findIdByEmail(email)
                .flatMap(userId -> replyRepository.save(Reply.newReply(content, userId, postId)))
                .then();
    }
}
