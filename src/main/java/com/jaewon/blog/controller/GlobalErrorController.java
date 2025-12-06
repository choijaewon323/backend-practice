package com.jaewon.blog.controller;

import com.jaewon.blog.common.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalErrorController {
    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<CommonResponse<String>>> handleException(RuntimeException e) {
        return Mono.just(ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(CommonResponse.fail(e.getLocalizedMessage())));
    }
}
