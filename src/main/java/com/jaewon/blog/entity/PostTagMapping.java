package com.jaewon.blog.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTagMapping {
    @Id
    private Long id;

    private Long postId;
    private Long tagId;

    public static PostTagMapping newMapping(Long postId, Long tagId) {
        return new PostTagMapping(null, postId, tagId);
    }
}
