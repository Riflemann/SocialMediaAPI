package com.socialmedia.socialmediaapi.models.auth;

import com.socialmedia.socialmediaapi.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String firstname;
  private String login;
  private String password;
  private Role role;
}
