package com.jaewon.blog.service;

import com.jaewon.blog.repository.PostTagMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class PostTagMappingService {
    private final PostTagMappingRepository postTagMappingRepository;

    public Flux<Long> getTagIdsByPostId(Long postId) {
        return postTagMappingRepository.findAllTagIdByPostId(postId);
    }
}
