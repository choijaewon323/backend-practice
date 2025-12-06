package com.jaewon.blog.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id
    private Long id;

    private String name;

    @CreatedDate
    private LocalDateTime createdAt;

    public static Category newCategory(String categoryName) {
        return new Category(null, categoryName, null);
    }
}
