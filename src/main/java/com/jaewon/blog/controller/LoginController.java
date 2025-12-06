package com.jaewon.blog.controller;

import com.jaewon.blog.common.CommonResponse;
import com.jaewon.blog.service.LoginService;
import com.jaewon.blog.util.StringValidationUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/login")
public class LoginController {
    private final LoginService loginService;

    @PostMapping
    public Mono<CommonResponse<Boolean>> login(@RequestBody LoginRequest request) {
        return loginService.login(request.email, request.password)
                .doFirst(request::validate)
                .map(CommonResponse::success);
    }

    @Getter
    @NoArgsConstructor
    public static class LoginRequest {
        private String email;
        private String password;

        public void validate() {
            StringValidationUtil.checkStringEmpty(email, "email");
            StringValidationUtil.checkStringEmpty(password, "password");
        }
    }
}
