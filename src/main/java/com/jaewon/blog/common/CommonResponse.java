package com.jaewon.blog.common;

import lombok.Getter;

@Getter
public class CommonResponse<T> {
    private final String status;
    private final T result;

    private CommonResponse(String status, T result) {
        this.status = status;
        this.result = result;
    }

    public static <T> CommonResponse<T> success(T result) {
        return new CommonResponse<>("success", result);
    }

    public static <T> CommonResponse<T> fail(T result) {
        return new CommonResponse<>("fail", result);
    }
}
