package com.socialmedia.socialmediaapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmedia.socialmediaapi.config.JwtService;
import com.socialmedia.socialmediaapi.models.User;
import com.socialmedia.socialmediaapi.repository.UserRepository;
import com.socialmedia.socialmediaapi.dto.AuthenticationRequest;
import com.socialmedia.socialmediaapi.models.AuthenticationResponse;
import com.socialmedia.socialmediaapi.dto.RegisterRequest;
import com.socialmedia.socialmediaapi.security.token.Token;
import com.socialmedia.socialmediaapi.security.token.TokenRepository;
import com.socialmedia.socialmediaapi.security.token.TokenType;
import com.socialmedia.socialmediaapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * <p>Класс AuthenticationService является сервисом для аутентификации пользователей. Он содержит методы для регистрации, аутентификации и обновления токенов пользователей.
 * <p>Поля:
 * <p>- repository - объект репозитория для работы с пользователями.
 * <p>- tokenRepository - объект репозитория для работы с токенами.
 * <p>- passwordEncoder - объект для кодирования паролей.
 * <p>- jwtService - объект для работы с JSON Web Token (JWT).
 * <p>- authenticationManager - объект для аутентификации пользователей.
 * <p>Методы:
 * <p>- Метод AuthenticationResponse register(RegisterRequest request)- Метод регистрации нового пользователя. Принимает на вход объект RegisterRequest, содержащий информацию о новом пользователе (имя, логин, пароль и роль). Создает нового пользователя, сохраняет его в базе данных, генерирует JWT-токен и обновляющий токен, сохраняет их в базе данных и возвращает объект AuthenticationResponse, содержащий JWT-токен и обновляющий токен.
 * <p>- Метод AuthenticationResponse authenticate(AuthenticationRequest request) - Метод аутентификации пользователя. Принимает на вход объект AuthenticationRequest, содержащий логин и пароль пользователя. Проверяет правильность логина и пароля, генерирует JWT-токен и обновляющий токен, сохраняет их в базе данных и возвращает объект AuthenticationResponse, содержащий JWT-токен и обновляющий токен.
 * <p>- Метод saveUserToken(User user, String jwtToken) - Метод сохранения токена пользователя в базе данных. Принимает на вход объект User и JWT-токен. Создает новый объект Token, содержащий информацию о пользователе и токене, и сохраняет его в базе данных.
 * <p>- Метод revokeAllUserTokens(User user) - Метод отзыва всех токенов пользователя. Принимает на вход объект User. Ищет все действующие токены пользователя, отзывает их и сохраняет изменения в базе данных.
 * <p>- Метод refreshToken(HttpServletRequest request, HttpServletResponse response) - Метод обновления JWT-токена. Принимает на вход объекты HttpServletRequest и HttpServletResponse. Извлекает из запроса обновляющий токен, извлекает из него логин пользователя, проверяет правильность токена, генерирует новый JWT-токен, отзывает все действующие токены пользователя, сохраняет новый JWT-токен и возвращает объект AuthenticationResponse, содержащий новый JWT-токен и обновляющий токен.*/
@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;

  private final UserService userService;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    var user = User.builder()
        .fullName(request.getFirstname())
        .login(request.getLogin())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(request.getRole())
        .build();
    var savedUser = userService.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getLogin(),
            request.getPassword()
        )
    );
    var user = repository.getUserByLogin(request.getLogin())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userLogin;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userLogin = jwtService.extractUsername(refreshToken);
    if (userLogin != null) {
      var user = this.repository.getUserByLogin(userLogin)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
