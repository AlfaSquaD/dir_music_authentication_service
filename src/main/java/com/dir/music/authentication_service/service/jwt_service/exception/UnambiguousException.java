package com.dir.music.authentication_service.service.jwt_service.exception;


import com.dir.music.authentication_service.service.foundation.IServiceException;

/**
 * This exception is thrown when don't want to other service or client know the reason of exception
 */
public class UnambiguousException extends IServiceException {
    public UnambiguousException(String message) {
        super(message);
    }
}
