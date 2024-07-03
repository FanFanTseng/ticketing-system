package com.fanfan.ticketingsystem.dto;

import java.time.LocalDate;
import lombok.Data;

/**
 * Project: ticketingsystem Created by: arten Date: 2024/7/1
 */
@Data
public class UserDTO {

  private String username;
  private String password;
  private String email;
  private LocalDate birthdate;

  // Getters and setters
}
