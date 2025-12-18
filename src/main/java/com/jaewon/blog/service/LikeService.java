package com.jaewon.blog.service;

import com.jaewon.blog.entity.like.Like;
import com.jaewon.blog.entity.like.LikeType;
import com.jaewon.blog.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;

    public Mono<Void> likePost(Long userId, Long postId) {
        return likeRepository.findByUserIdAndTargetIdAndType(userId, postId, LikeType.POST)
                .flatMap(like -> {
                    like.like();

                    return likeRepository.save(like);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    Like postLike = Like.newPostLike(userId, postId);

                    return likeRepository.save(postLike);
                }))
                .then();
    }

    public Mono<Void> dislikePost(Long userId, Long postId) {
        return likeRepository.findByUserIdAndTargetIdAndType(userId, postId, LikeType.POST)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("해당 게시글 좋아요 취소에 실패했습니다")))
                .flatMap(like -> {
                    like.dislike();
                    return likeRepository.save(like);
                })
                .then();
    }

    public Mono<Void> likeReply(Long userId, Long replyId) {
        return likeRepository.findByUserIdAndTargetIdAndType(userId, replyId, LikeType.REPLY)
                .flatMap(like -> {
                    like.like();

                    return likeRepository.save(like);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    Like postLike = Like.newPostLike(userId, replyId);

                    return likeRepository.save(postLike);
                }))
                .then();
    }

    public Mono<Void> dislikeReply(Long userId, Long replyId) {
        return likeRepository.findByUserIdAndTargetIdAndType(userId, replyId, LikeType.REPLY)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("해당 댓글 좋아요 취소에 실패했습니다")))
                .flatMap(like -> {
                    like.dislike();
                    return likeRepository.save(like);
                })
                .then();
    }

    public Mono<Void> deleteLikesByUserId(Long userId) {
        return likeRepository.deleteAllByUserId(userId);
    }
}
