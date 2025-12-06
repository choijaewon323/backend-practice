package com.jaewon.blog.service;

import com.jaewon.blog.entity.Tag;
import com.jaewon.blog.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public Mono<Long> getTagIdByName(String name) {
        return tagRepository.findIdByName(name)
                .switchIfEmpty(tagRepository.save(Tag.newTag(name))
                        .map(Tag::getId));
    }

    public Flux<String> getTagNamesFromTagId(List<Long> tagIds) {
        return tagRepository.findAllById(tagIds)
                .map(Tag::getName);
    }
}
