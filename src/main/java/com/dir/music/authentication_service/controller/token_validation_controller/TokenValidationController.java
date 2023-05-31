package com.dir.music.authentication_service.controller.token_validation_controller;


import com.dir.music.authentication_service.controller.token_validation_controller.input.TokenValidationControllerInput;
import com.dir.music.authentication_service.controller.token_validation_controller.output.TokenValidationControllerOutput;
import com.dir.music.authentication_service.service.jwt_service.JWTService;
import com.dir.music.authentication_service.service.jwt_service.exception.TokenExpiredException;
import com.dir.music.authentication_service.service.jwt_service.exception.UnambiguousException;
import com.dir.music.authentication_service.service.jwt_service.input.JWTServiceAccessTokenInput;
import com.dir.music.authentication_service.service.jwt_service.output.JWTServiceAccessTokenClaimsOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("TokenValidationController")
@RequestMapping("/token-validation")
public class TokenValidationController {
    final JWTService jwtService;

    @Autowired
    public TokenValidationController(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping(path = "/get-claims", consumes = "application/json", produces = "application/json")
    public ResponseEntity<TokenValidationControllerOutput> getClaims(
            @RequestBody TokenValidationControllerInput tokenValidationControllerInput
    ) {
        final String accessToken = tokenValidationControllerInput.getAccessToken();

        final JWTServiceAccessTokenClaimsOutput jwtServiceAccessTokenClaimsOutput;

        try {
            jwtServiceAccessTokenClaimsOutput
                    = this.jwtService.getClaims(JWTServiceAccessTokenInput.builder()
                    .accessToken(accessToken).build());
        } catch (UnambiguousException | TokenExpiredException e) {
            if (e instanceof UnambiguousException) {
                return ResponseEntity.badRequest().build();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }

        final TokenValidationControllerOutput tokenValidationControllerOutput
                = TokenValidationControllerOutput.builder()
                .username(jwtServiceAccessTokenClaimsOutput.getUsername())
                .role(jwtServiceAccessTokenClaimsOutput.getRole())
                .build();

        return ResponseEntity.ok(tokenValidationControllerOutput);
    }
}
