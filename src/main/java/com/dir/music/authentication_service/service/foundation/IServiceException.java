package com.dir.music.authentication_service.service.foundation;

public abstract class IServiceException extends Exception{
    public IServiceException(String message) {
        super(message);
    }
}
