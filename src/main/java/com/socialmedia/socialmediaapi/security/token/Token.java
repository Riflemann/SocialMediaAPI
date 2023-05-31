package com.socialmedia.socialmediaapi.security.token;

import com.socialmedia.socialmediaapi.models.User;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Token {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Integer id;

  @Column(unique = true)
  public String token;

  @Enumerated(EnumType.STRING)
  public TokenType tokenType = TokenType.BEARER;

  public boolean revoked;

  public boolean expired;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  public User user;

  public Token(Integer id, String token, TokenType tokenType, boolean revoked, boolean expired, User user) {
    this.id = id;
    this.token = token;
    this.tokenType = tokenType;
    this.revoked = revoked;
    this.expired = expired;
    this.user = user;
  }

  public Token() {
  }

  public static TokenBuilder builder() {
    return new TokenBuilder();
  }

  public Integer getId() {
    return this.id;
  }

  public String getToken() {
    return this.token;
  }

  public TokenType getTokenType() {
    return this.tokenType;
  }

  public boolean isRevoked() {
    return this.revoked;
  }

  public boolean isExpired() {
    return this.expired;
  }

  public User getUser() {
    return this.user;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public void setTokenType(TokenType tokenType) {
    this.tokenType = tokenType;
  }

  public void setRevoked(boolean revoked) {
    this.revoked = revoked;
  }

  public void setExpired(boolean expired) {
    this.expired = expired;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Token token1 = (Token) o;
    return revoked == token1.revoked && expired == token1.expired && Objects.equals(id, token1.id) && Objects.equals(token, token1.token) && tokenType == token1.tokenType && Objects.equals(user, token1.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, token, tokenType, revoked, expired, user);
  }

  public String toString() {
    return "Token(id=" + this.getId() + ", token=" + this.getToken() + ", tokenType=" + this.getTokenType() + ", revoked=" + this.isRevoked() + ", expired=" + this.isExpired() + ", user=" + this.getUser() + ")";
  }

  public static class TokenBuilder {
    private Integer id;
    private String token;
    private TokenType tokenType;
    private boolean revoked;
    private boolean expired;
    private User user;

    TokenBuilder() {
    }

    public TokenBuilder id(Integer id) {
      this.id = id;
      return this;
    }

    public TokenBuilder token(String token) {
      this.token = token;
      return this;
    }

    public TokenBuilder tokenType(TokenType tokenType) {
      this.tokenType = tokenType;
      return this;
    }

    public TokenBuilder revoked(boolean revoked) {
      this.revoked = revoked;
      return this;
    }

    public TokenBuilder expired(boolean expired) {
      this.expired = expired;
      return this;
    }

    public TokenBuilder user(User user) {
      this.user = user;
      return this;
    }

    public Token build() {
      return new Token(id, token, tokenType, revoked, expired, user);
    }
  }
}
