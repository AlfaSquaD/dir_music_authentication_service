package com.dir.music.authentication_service.service.jwt_service;

import com.dir.music.authentication_service.service.foundation.IService;
import com.dir.music.authentication_service.service.jwt_service.exception.TokenExpiredException;
import com.dir.music.authentication_service.service.jwt_service.exception.UnambiguousException;
import com.dir.music.authentication_service.service.jwt_service.input.JWTServiceAccessTokenInput;
import com.dir.music.authentication_service.service.jwt_service.input.JWTServiceRefreshTokenInput;
import com.dir.music.authentication_service.service.jwt_service.input.JWTServiceUserAuthenticationInput;
import com.dir.music.authentication_service.service.jwt_service.output.JWTServiceAccessTokenClaimsOutput;
import com.dir.music.authentication_service.service.jwt_service.output.JWTServiceRefreshTokenClaimsOutput;
import com.dir.music.authentication_service.service.jwt_service.output.JWTServiceUserAuthenticationOutput;

public interface JWTService extends IService {

    JWTServiceUserAuthenticationOutput generateToken(JWTServiceUserAuthenticationInput jwtServiceUserAuthenticationInput);

    JWTServiceAccessTokenClaimsOutput getClaims(JWTServiceAccessTokenInput accessTokenInput)
            throws TokenExpiredException, UnambiguousException;

    JWTServiceRefreshTokenClaimsOutput getClaims(JWTServiceRefreshTokenInput refreshTokenInput)
            throws TokenExpiredException, UnambiguousException;


}
