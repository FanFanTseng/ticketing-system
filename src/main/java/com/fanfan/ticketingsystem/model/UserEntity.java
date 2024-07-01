package com.fanfan.ticketingsystem.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Set;
import lombok.Data;


/**
 * Project: ticketingsystem Created by: arten Date: 2024/7/1
 */
@Entity
@Data
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;
  private String password;
  private String email;

  @ElementCollection(fetch = FetchType.EAGER)
  private Set<String> roles;

  // Getters and setters
}
