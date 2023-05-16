package com.dir.music.authentication_service.service.password_service;

import com.dir.music.authentication_service.core.configuration.AuthenticationServiceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {
    final BCryptPasswordEncoder encoder;
    final AuthenticationServiceConfiguration authenticationServiceConfiguration;

    @Autowired
    public PasswordServiceImpl(AuthenticationServiceConfiguration authenticationServiceConfiguration) {
        this.authenticationServiceConfiguration = authenticationServiceConfiguration;
        this.encoder = new BCryptPasswordEncoder(authenticationServiceConfiguration.getPasswordHashStrength());
    }

    public String hashPassword(String password) {
        return encoder.encode(password);
    }

    public boolean isPasswordValid(String password, String hashedPassword) {
        return encoder.matches(password, hashedPassword);
    }
}
