package com.dir.music.authentication_service.service.auth_service.exception;

import com.dir.music.authentication_service.service.foundation.IServiceException;

public class InvalidCredentialsException extends IServiceException {
    public InvalidCredentialsException() {
        super("Invalid credentials");
    }
}
