package com.fanfan.ticketingsystem.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Project: ticketingsystem Created by: arten Date: 2024/7/1
 */
@Component
public class PasswordEncoderUtil {

  private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public static String encode(String rawPassword) {
    return passwordEncoder.encode(rawPassword);
  }

  public static boolean matches(String rawPassword, String encodedPassword) {
    return passwordEncoder.matches(rawPassword, encodedPassword);
  }
}