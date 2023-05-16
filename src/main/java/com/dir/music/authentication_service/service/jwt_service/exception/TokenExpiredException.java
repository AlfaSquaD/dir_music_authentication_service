package com.dir.music.authentication_service.service.jwt_service.exception;

import com.dir.music.authentication_service.service.foundation.IServiceException;

public class TokenExpiredException extends IServiceException {
    public TokenExpiredException() {
        super("Token expired");
    }
}
