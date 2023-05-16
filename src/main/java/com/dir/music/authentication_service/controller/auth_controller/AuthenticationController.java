package com.dir.music.authentication_service.controller.auth_controller;

import com.dir.music.authentication_service.controller.auth_controller.input.AuthControllerChangePasswordInput;
import com.dir.music.authentication_service.controller.auth_controller.input.AuthControllerPasswordLoginInput;
import com.dir.music.authentication_service.controller.auth_controller.input.AuthControllerRegisterInput;
import com.dir.music.authentication_service.controller.auth_controller.output.AuthControllerAccessTokenOutput;
import com.dir.music.authentication_service.controller.auth_controller.output.AuthControllerPasswordLoginOutput;
import com.dir.music.authentication_service.controller.auth_controller.output.RegisterOutput;
import com.dir.music.authentication_service.service.auth_service.AuthService;
import com.dir.music.authentication_service.service.auth_service.exception.InvalidCredentialsException;
import com.dir.music.authentication_service.service.auth_service.input.AuthServiceChangePasswordInput;
import com.dir.music.authentication_service.service.auth_service.input.AuthServicePasswordLoginInput;
import com.dir.music.authentication_service.service.auth_service.input.AuthServiceRefreshInput;
import com.dir.music.authentication_service.service.auth_service.input.AuthServiceRegisterInput;
import com.dir.music.authentication_service.service.auth_service.output.AuthServiceChangePasswordOutput;
import com.dir.music.authentication_service.service.auth_service.output.AuthServicePasswordLoginOutput;
import com.dir.music.authentication_service.service.auth_service.output.AuthServiceRefreshOutput;
import com.dir.music.authentication_service.service.auth_service.output.AuthServiceRegisterOutput;
import com.dir.music.authentication_service.service.jwt_service.exception.TokenExpiredException;
import com.dir.music.authentication_service.service.jwt_service.exception.UnambiguousException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("AuthenticationController")
@RequestMapping("/")
public class AuthenticationController {
    private final AuthService authService;

    @Autowired
    public AuthenticationController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<RegisterOutput> register(
            @RequestBody AuthControllerRegisterInput authControllerRegisterInput
    ) {
        final AuthServiceRegisterInput authServiceRegisterInput = AuthServiceRegisterInput.builder()
                .username(authControllerRegisterInput.getUsername())
                .password(authControllerRegisterInput.getPassword())
                .role("USER")
                .build();

        final AuthServiceRegisterOutput authServiceRegisterOutput = this.authService.register(authServiceRegisterInput);

        final RegisterOutput registerOutput = RegisterOutput.builder()
                .success(authServiceRegisterOutput.isSuccess())
                .username(authServiceRegisterOutput.getUsername())
                .id(authServiceRegisterOutput.getId())
                .build();

        return ResponseEntity.ok(registerOutput);
    }

    @PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<AuthControllerPasswordLoginOutput> login(
            @RequestBody AuthControllerPasswordLoginInput authInput
    ) {
        final AuthServicePasswordLoginInput authServicePasswordLoginInput = AuthServicePasswordLoginInput.builder()
                .username(authInput.getUsername())
                .password(authInput.getPassword())
                .build();
        final AuthServicePasswordLoginOutput authServicePasswordLoginOutput;
        try {
            authServicePasswordLoginOutput = this.authService.
                    loginWithPassword(authServicePasswordLoginInput);
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final AuthControllerPasswordLoginOutput authControllerPasswordLoginOutput = AuthControllerPasswordLoginOutput.builder()
                .accessToken(authServicePasswordLoginOutput.getAccessToken())
                .refreshToken(authServicePasswordLoginOutput.getRefreshToken())
                .build();

        return ResponseEntity.ok(authControllerPasswordLoginOutput);
    }

    @GetMapping(path = "/refresh", produces = "application/json")
    public ResponseEntity<AuthControllerAccessTokenOutput> refreshToken(
            @RequestParam(name = "refreshToken") String refreshToken
    ) {
        final AuthServiceRefreshInput authServiceRefreshInput = AuthServiceRefreshInput.builder()
                .refreshToken(refreshToken)
                .build();
        final AuthServiceRefreshOutput authServiceRefreshOutput;
        try {
            authServiceRefreshOutput = this.authService.refreshToken(authServiceRefreshInput);

        } catch (UnambiguousException | TokenExpiredException e) {
            if (e instanceof UnambiguousException) {
                return ResponseEntity.badRequest().build();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        final AuthControllerAccessTokenOutput authControllerAccessTokenOutput = AuthControllerAccessTokenOutput.builder()
                .accessToken(authServiceRefreshOutput.getAccessToken())
                .build();

        return ResponseEntity.ok(authControllerAccessTokenOutput);
    }

    @PostMapping(path = "/change-password", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> changePassword(
            @RequestBody AuthControllerChangePasswordInput authControllerChangePasswordInput
    ) {
        final AuthServiceChangePasswordInput authServiceChangePasswordInput = AuthServiceChangePasswordInput.builder()
                .username(authControllerChangePasswordInput.getUsername())
                .oldPassword(authControllerChangePasswordInput.getOldPassword())
                .newPassword(authControllerChangePasswordInput.getNewPassword())
                .build();

        final AuthServiceChangePasswordOutput authServiceChangePasswordOutput =
                this.authService.changePassword(authServiceChangePasswordInput);

        if (authServiceChangePasswordOutput.isSuccess()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
