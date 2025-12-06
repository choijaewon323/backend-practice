package com.jaewon.blog.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

/*
    태그가 존재한다.
    태그는 이름이 있다.
    하나의 게시글에 여러 태그를 달 수 있다.
    하나의 태그는 여러 게시글에 달릴 수 있다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
    @Id
    private Long id;

    private String name;

    @CreatedDate
    private LocalDateTime createdAt;

    private Tag(Long id, String name, LocalDateTime createdAt) {
        checkNameUnder50(name);

        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public static Tag newTag(String name) {
        return new Tag(null, name, null);
    }

    private void checkNameUnder50(String name) {
        if (name.length() > 50) {
            throw new IllegalArgumentException("태그는 50글자를 넘길 수 없습니다");
        }
    }
}
