package com.jaewon.blog.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    private Long id;

    private String email;
    private String password;
    private String nickname;

    @CreatedDate
    private LocalDateTime createdAt;

    public User(Long id, String email, String password, String nickname, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.createdAt = createdAt;
    }

    public static User newUser(String email, String password, String nickname) {
        return new User(null, email, password, nickname, null);
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateNickname(String newNickname) {
        this.nickname = nickname;
    }

    public boolean isPasswordSame(String password) {
        return this.password.equals(password);
    }
}
