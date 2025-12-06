package com.jaewon.blog.entity.like;

import lombok.Getter;

@Getter
public enum LikeType {
    POST("post"),
    REPLY("reply")
    ;

    private final String type;

    LikeType(String type) {
        this.type = type;
    }
}
