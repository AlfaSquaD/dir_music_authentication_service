package com.dir.music.authentication_service.controller.token_validation_controller.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenValidationControllerInput {
    private String accessToken;
}
