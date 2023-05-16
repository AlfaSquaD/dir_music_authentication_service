package com.dir.music.authentication_service.service.auth_service;

import com.dir.music.authentication_service.repository.user_repository.UserModel;
import com.dir.music.authentication_service.repository.user_repository.UserRepository;
import com.dir.music.authentication_service.service.auth_service.exception.InvalidCredentialsException;
import com.dir.music.authentication_service.service.auth_service.input.AuthServiceChangePasswordInput;
import com.dir.music.authentication_service.service.auth_service.input.AuthServicePasswordLoginInput;
import com.dir.music.authentication_service.service.auth_service.input.AuthServiceRefreshInput;
import com.dir.music.authentication_service.service.auth_service.input.AuthServiceRegisterInput;
import com.dir.music.authentication_service.service.auth_service.output.AuthServiceChangePasswordOutput;
import com.dir.music.authentication_service.service.auth_service.output.AuthServicePasswordLoginOutput;
import com.dir.music.authentication_service.service.auth_service.output.AuthServiceRefreshOutput;
import com.dir.music.authentication_service.service.auth_service.output.AuthServiceRegisterOutput;
import com.dir.music.authentication_service.service.jwt_service.JWTService;
import com.dir.music.authentication_service.service.jwt_service.exception.TokenExpiredException;
import com.dir.music.authentication_service.service.jwt_service.exception.UnambiguousException;
import com.dir.music.authentication_service.service.jwt_service.input.JWTServiceRefreshTokenInput;
import com.dir.music.authentication_service.service.jwt_service.input.JWTServiceUserAuthenticationInput;
import com.dir.music.authentication_service.service.jwt_service.output.JWTServiceRefreshTokenClaimsOutput;
import com.dir.music.authentication_service.service.jwt_service.output.JWTServiceUserAuthenticationOutput;
import com.dir.music.authentication_service.service.password_service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl implements AuthService {
    final PasswordService passwordService;
    final UserRepository userRepository;
    final JWTService jwtService;

    @Autowired
    public AuthServiceImpl(PasswordService passwordService, UserRepository userRepository, JWTService jwtService) {
        this.passwordService = passwordService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public AuthServicePasswordLoginOutput loginWithPassword(AuthServicePasswordLoginInput input)
            throws InvalidCredentialsException {
        final UserModel userModel = userRepository.findByUsername(input.getUsername());
        if (userModel == null) {
            throw new InvalidCredentialsException();
        }
        if (!passwordService.isPasswordValid(input.getPassword(), userModel.getPassword())) {
            throw new InvalidCredentialsException();
        }
        final JWTServiceUserAuthenticationInput jwtServiceUserAuthenticationInput =
                JWTServiceUserAuthenticationInput.builder()
                        .userModel(userModel).build();
        final JWTServiceUserAuthenticationOutput jwtServiceUserAuthenticationOutput =
                jwtService.generateToken(jwtServiceUserAuthenticationInput);

        return AuthServicePasswordLoginOutput.builder()
                .accessToken(jwtServiceUserAuthenticationOutput.getAccessToken())
                .refreshToken(jwtServiceUserAuthenticationOutput.getRefreshToken())
                .build();
    }

    public AuthServiceRefreshOutput refreshToken(AuthServiceRefreshInput input) throws TokenExpiredException, UnambiguousException {
        final JWTServiceRefreshTokenClaimsOutput jwtServiceUserAuthenticationOutput =
                jwtService.getClaims(JWTServiceRefreshTokenInput.builder().refreshToken(input.getRefreshToken()).build());

        final UserModel userModel = userRepository.findById(jwtServiceUserAuthenticationOutput.getSubject());

        final JWTServiceUserAuthenticationInput jwtServiceUserAuthenticationInput =
                JWTServiceUserAuthenticationInput.builder()
                        .userModel(userModel).build();

        return AuthServiceRefreshOutput.builder()
                .accessToken(jwtService.generateToken(jwtServiceUserAuthenticationInput).getAccessToken())
                .build();
    }

    public AuthServiceRegisterOutput register(AuthServiceRegisterInput input) {
        if (userRepository.existsByUsername(input.getUsername())) {
            return AuthServiceRegisterOutput.builder()
                    .success(false)
                    .build();
        }
        final UserModel userModel = UserModel.builder()
                .username(input.getUsername())
                .enabled(true)
                .password(passwordService.hashPassword(input.getPassword()))
                .role(input.getRole())
                .build();
        userRepository.save(userModel);
        return AuthServiceRegisterOutput.builder()
                .success(true)
                .id(userModel.getId())
                .username(userModel.getUsername())
                .build();
    }

    public AuthServiceChangePasswordOutput changePassword(AuthServiceChangePasswordInput input) {
        final UserModel userModel = userRepository.findByUsername(input.getUsername());
        if (userModel == null) {
            return AuthServiceChangePasswordOutput.builder()
                    .success(false)
                    .build();
        }
        if (!passwordService.isPasswordValid(input.getOldPassword(), userModel.getPassword())) {
            return AuthServiceChangePasswordOutput.builder()
                    .success(false)
                    .build();
        }
        userModel.setPassword(passwordService.hashPassword(input.getNewPassword()));
        userRepository.save(userModel);
        return AuthServiceChangePasswordOutput.builder()
                .success(true)
                .build();
    }


}
