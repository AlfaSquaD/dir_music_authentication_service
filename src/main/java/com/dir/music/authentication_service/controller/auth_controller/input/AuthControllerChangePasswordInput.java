package com.dir.music.authentication_service.controller.auth_controller.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AuthControllerChangePasswordInput {
    private String username;
    private String oldPassword;
    private String newPassword;
}
