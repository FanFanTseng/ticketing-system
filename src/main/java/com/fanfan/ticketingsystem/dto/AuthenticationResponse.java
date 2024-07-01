package com.fanfan.ticketingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Project: ticketingsystem Created by: arten Date: 2024/7/1
 */
@Data
@AllArgsConstructor
public class AuthenticationResponse {

  private String jwt;
}

