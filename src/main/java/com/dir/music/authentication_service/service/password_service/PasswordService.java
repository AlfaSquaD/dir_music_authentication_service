package com.dir.music.authentication_service.service.password_service;

import com.dir.music.authentication_service.service.foundation.IService;


public interface PasswordService extends IService {
    String hashPassword(String password);

    boolean isPasswordValid(String password, String hashedPassword);
}
