package com.dir.music.authentication_service.service.jwt_service;

import com.dir.music.authentication_service.core.configuration.AuthenticationServiceConfiguration;
import com.dir.music.authentication_service.repository.user_repository.UserModel;
import com.dir.music.authentication_service.service.jwt_service.exception.TokenExpiredException;
import com.dir.music.authentication_service.service.jwt_service.exception.UnambiguousException;
import com.dir.music.authentication_service.service.jwt_service.input.JWTServiceAccessTokenInput;
import com.dir.music.authentication_service.service.jwt_service.input.JWTServiceRefreshTokenInput;
import com.dir.music.authentication_service.service.jwt_service.input.JWTServiceUserAuthenticationInput;
import com.dir.music.authentication_service.service.jwt_service.output.JWTServiceAccessTokenClaimsOutput;
import com.dir.music.authentication_service.service.jwt_service.output.JWTServiceRefreshTokenClaimsOutput;
import com.dir.music.authentication_service.service.jwt_service.output.JWTServiceUserAuthenticationOutput;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;

@Service
public class JWTServiceImpl implements JWTService {
    final private AuthenticationServiceConfiguration authenticationServiceConfiguration;

    @Autowired
    public JWTServiceImpl(AuthenticationServiceConfiguration authenticationServiceConfiguration) {
        this.authenticationServiceConfiguration = authenticationServiceConfiguration;
    }

    public JWTServiceUserAuthenticationOutput generateToken(JWTServiceUserAuthenticationInput jwtServiceUserAuthenticationInput) {
        final UserModel userModel = jwtServiceUserAuthenticationInput.getUserModel();
        final HashMap<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", userModel.getRole());
        final String accessToken = Jwts.builder()
                .setSubject(userModel.getId().toString())
                .setIssuer(authenticationServiceConfiguration.getJwtIssuer())
                .setAudience(authenticationServiceConfiguration.getJwtAudience())
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .setIssuedAt(Date.from(Instant.now()))
                .addClaims(extraClaims)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, authenticationServiceConfiguration.getJwtAccessTokenSecret())
                .compact();

        final String refreshToken = Jwts.builder()
                .setSubject(userModel.getId().toString())
                .setIssuer(authenticationServiceConfiguration.getJwtIssuer())
                .setAudience(authenticationServiceConfiguration.getJwtAudience())
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, authenticationServiceConfiguration.getJwtRefreshTokenSecret())
                .compact();

        return JWTServiceUserAuthenticationOutput.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken).build();
    }

    public JWTServiceAccessTokenClaimsOutput getClaims(JWTServiceAccessTokenInput accessTokenInput)
            throws TokenExpiredException, UnambiguousException {
        Jws<Claims> jwt;
        String token = accessTokenInput.getAccessToken();
        token = token.substring(7);
        try {
            jwt = _getClaims(token, authenticationServiceConfiguration.getJwtAccessTokenSecret());
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            if (e instanceof ExpiredJwtException) {
                throw new TokenExpiredException();
            }
            throw new UnambiguousException("Something went wrong.");
        }

        return JWTServiceAccessTokenClaimsOutput.builder()
                .audience(jwt.getBody().getAudience())
                .issuer(jwt.getBody().getIssuer())
                .expiration(jwt.getBody().getExpiration().getTime())
                .issuedAt(jwt.getBody().getIssuedAt().getTime())
                .role(jwt.getBody().get("role", String.class))
                .username(jwt.getBody().getSubject())
                .subject(Long.parseLong(jwt.getBody().getSubject()))
                .build();
    }

    public JWTServiceRefreshTokenClaimsOutput getClaims(JWTServiceRefreshTokenInput refreshTokenInput)
            throws TokenExpiredException, UnambiguousException {
        Jws<Claims> jwt;
        String token = refreshTokenInput.getRefreshToken();
        try {
            jwt = _getClaims(token, authenticationServiceConfiguration.getJwtRefreshTokenSecret());
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            if (e instanceof ExpiredJwtException) {
                throw new TokenExpiredException();
            }
            throw new UnambiguousException("Something went wrong.");
        }

        return JWTServiceRefreshTokenClaimsOutput.builder()
                .audience(jwt.getBody().getAudience())
                .issuer(jwt.getBody().getIssuer())
                .issuedAt(jwt.getBody().getIssuedAt().getTime())
                .subject(Long.parseLong(jwt.getBody().getSubject()))
                .build();
    }


    private Jws<Claims> _getClaims(String token, String secret) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token);
    }
}
