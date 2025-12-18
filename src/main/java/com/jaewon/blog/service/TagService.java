package com.jaewon.blog.service;

import com.jaewon.blog.entity.Tag;
import com.jaewon.blog.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public Flux<String> getTagNamesFromTagId(List<Long> tagIds) {
        return tagRepository.findAllById(tagIds)
                .map(Tag::getName);
    }
}
