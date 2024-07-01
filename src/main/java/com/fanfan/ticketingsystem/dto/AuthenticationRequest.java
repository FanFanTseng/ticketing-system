package com.fanfan.ticketingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Project: ticketingsystem Created by: arten Date: 2024/7/1
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

  private String username;
  private String password;

  // Getters and setters
}

