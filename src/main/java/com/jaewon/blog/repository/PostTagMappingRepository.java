package com.jaewon.blog.repository;

import com.jaewon.blog.entity.PostTagMapping;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PostTagMappingRepository extends ReactiveCrudRepository<PostTagMapping, Long> {
    Flux<Long> findAllPostIdByTagId(Long tagId);
    Flux<Long> findAllTagIdByPostId(Long postId);
}
