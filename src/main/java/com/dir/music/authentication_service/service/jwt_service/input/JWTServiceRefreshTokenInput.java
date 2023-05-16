package com.dir.music.authentication_service.service.jwt_service.input;

import com.dir.music.authentication_service.service.foundation.IServiceInput;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JWTServiceRefreshTokenInput implements IServiceInput {
    private String refreshToken;
}
