package com.jaewon.blog.controller;

import com.jaewon.blog.common.CommonResponse;
import com.jaewon.blog.service.user.UserDeleteService;
import com.jaewon.blog.service.user.UserService;
import com.jaewon.blog.util.StringValidationUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserDeleteService userDeleteService;
    private final UserService userService;

    @PostMapping
    public Mono<CommonResponse<Boolean>> createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request.email, request.password, request.nickname)
                .doFirst(request::validate)
                .map(CommonResponse::success);
    }

    @PutMapping
    public Mono<CommonResponse<Boolean>> updatePassword(@RequestBody UpdateUserPasswordRequest request) {
        return userService.updatePassword(request.email, request.oldPassword, request.newPassword)
                .doFirst(request::validate)
                .map(CommonResponse::success);
    }

    @DeleteMapping("{email}")
    public Mono<CommonResponse<Boolean>> deleteUser(@PathVariable String email) {
        return userDeleteService.deleteUserAndBoardsAndReplies(email)
                .doFirst(() -> StringValidationUtil.checkStringEmpty(email, "email"))
                .thenReturn(true)
                .map(CommonResponse::success)
                .onErrorReturn(CommonResponse.fail(false));
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateUserPasswordRequest {
        private String email;
        private String oldPassword;
        private String newPassword;

        public void validate() {
            StringValidationUtil.checkStringEmpty(email, "email");
            StringValidationUtil.checkStringEmpty(oldPassword, "oldPassword");
            StringValidationUtil.checkStringEmpty(newPassword, "newPassword");
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CreateUserRequest {
        private String email;
        private String password;
        private String nickname;

        public void validate() {
            StringValidationUtil.checkStringEmpty(email, "email");
            StringValidationUtil.checkStringEmpty(password, "password");
            StringValidationUtil.checkStringEmpty(nickname, "nickname");
        }
    }
}
