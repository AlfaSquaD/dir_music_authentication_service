package com.dir.music.authentication_service.service;


import com.dir.music.authentication_service.core.configuration.AuthenticationServiceConfiguration;
import com.dir.music.authentication_service.service.password_service.PasswordService;
import com.dir.music.authentication_service.service.password_service.PasswordServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PasswordServiceTest {
    @Mock
    private AuthenticationServiceConfiguration authenticationServiceConfiguration;

    @Before
    public void before() {
        Mockito.when(authenticationServiceConfiguration.getPasswordHashStrength()).thenReturn(10);
    }

    @Test
    public void testHashPassword() {
        final PasswordService passwordService = new PasswordServiceImpl(authenticationServiceConfiguration);
        final String password = "password";
        try {
            final String hashedPassword = passwordService.hashPassword(password);
            assert (passwordService.isPasswordValid(password, hashedPassword));
        } catch (Exception e) {
            assert (false);
        }
    }

}
