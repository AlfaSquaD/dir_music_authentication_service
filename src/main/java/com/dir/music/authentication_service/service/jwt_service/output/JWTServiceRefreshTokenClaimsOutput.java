package com.dir.music.authentication_service.service.jwt_service.output;

import com.dir.music.authentication_service.service.foundation.IServiceOutput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JWTServiceRefreshTokenClaimsOutput implements IServiceOutput {
    private long subject;
    private String issuer;
    private String audience;
    private long issuedAt;

}
