package com.socialmedia.socialmediaapi.service.impl;

import com.socialmedia.socialmediaapi.config.JwtService;
import com.socialmedia.socialmediaapi.dto.AuthenticationRequest;
import com.socialmedia.socialmediaapi.dto.RegisterRequest;
import com.socialmedia.socialmediaapi.models.AuthenticationResponse;
import com.socialmedia.socialmediaapi.models.Role;
import com.socialmedia.socialmediaapi.models.User;
import com.socialmedia.socialmediaapi.repository.UserRepository;
import com.socialmedia.socialmediaapi.security.token.Token;
import com.socialmedia.socialmediaapi.security.token.TokenRepository;
import com.socialmedia.socialmediaapi.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerShouldCreateUserAndReturnTokens() throws UserExistException {
        RegisterRequest request = new RegisterRequest("John Doe", "johndoe", "password", Role.USER);
        User user = User.builder()
                .fullName("John Doe")
                .login("johndoe")
                .password("encodedPassword")
                .role(Role.USER)
                .build();
        when(userRepository.save(user)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        AuthenticationResponse response = authenticationService.register(request);

        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void authenticateShouldAuthenticateUserAndReturnTokens() {
        AuthenticationRequest request = new AuthenticationRequest("johndoe", "password");
        User user = User.builder()
                .fullName("John Doe")
                .login("johndoe")
                .password("encodedPassword")
                .role(Role.USER)
                .build();
        when(userRepository.getUserByLogin("johndoe")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void refreshTokenShouldRefreshTokens() throws IOException {
        String refreshToken = "refreshToken";
        String accessToken = "newAccessToken";
        User user = User.builder()
                .fullName("John Doe")
                .login("johndoe")
                .password("encodedPassword")
                .role(Role.USER)
                .build();
        when(request.getHeader("Authorization")).thenReturn("Bearer " + refreshToken);
        when(jwtService.extractUsername(refreshToken)).thenReturn("johndoe");
        when(userRepository.getUserByLogin("johndoe")).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(refreshToken, user)).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn(accessToken);

        authenticationService.refreshToken(request, response);

        verify(tokenRepository).save(any(Token.class));
        verify(response).getOutputStream();
        verify(response.getOutputStream()).write(any(byte[].class));
    }
}