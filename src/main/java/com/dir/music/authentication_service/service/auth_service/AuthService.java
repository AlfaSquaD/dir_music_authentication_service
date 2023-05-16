package com.dir.music.authentication_service.service.auth_service;

import com.dir.music.authentication_service.service.auth_service.exception.InvalidCredentialsException;
import com.dir.music.authentication_service.service.auth_service.input.AuthServiceChangePasswordInput;
import com.dir.music.authentication_service.service.auth_service.input.AuthServicePasswordLoginInput;
import com.dir.music.authentication_service.service.auth_service.input.AuthServiceRefreshInput;
import com.dir.music.authentication_service.service.auth_service.input.AuthServiceRegisterInput;
import com.dir.music.authentication_service.service.auth_service.output.AuthServiceChangePasswordOutput;
import com.dir.music.authentication_service.service.auth_service.output.AuthServicePasswordLoginOutput;
import com.dir.music.authentication_service.service.auth_service.output.AuthServiceRefreshOutput;
import com.dir.music.authentication_service.service.auth_service.output.AuthServiceRegisterOutput;
import com.dir.music.authentication_service.service.foundation.IService;
import com.dir.music.authentication_service.service.jwt_service.exception.TokenExpiredException;
import com.dir.music.authentication_service.service.jwt_service.exception.UnambiguousException;


public interface AuthService extends IService {


    AuthServicePasswordLoginOutput loginWithPassword(AuthServicePasswordLoginInput input) throws InvalidCredentialsException;

    AuthServiceRefreshOutput refreshToken(AuthServiceRefreshInput input) throws TokenExpiredException, UnambiguousException;

    AuthServiceRegisterOutput register(AuthServiceRegisterInput input);

    AuthServiceChangePasswordOutput changePassword(AuthServiceChangePasswordInput input);
}
