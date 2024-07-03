package com.fanfan.ticketingsystem.controller;

import com.fanfan.ticketingsystem.dto.AuthenticationRequest;
import com.fanfan.ticketingsystem.dto.AuthenticationResponse;
import com.fanfan.ticketingsystem.dto.UserDTO;
import com.fanfan.ticketingsystem.model.UserEntity;
import com.fanfan.ticketingsystem.util.JwtUtil;
import com.fanfan.ticketingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.AuthenticationException;


/**
 * Project: ticketingsystem Created by: arten Date: 2024/7/1
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserService userService;

  @Autowired
  private JwtUtil jwtUtil;

  @PostMapping("/login")
  public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
      );
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (AuthenticationException e) {
      throw new Exception("Incorrect username or password", e);
    }

    final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());
    final String jwt = jwtUtil.generateToken(userDetails);

    return ResponseEntity.ok(new AuthenticationResponse(jwt));
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
    if (userService.findByUsername(userDTO.getUsername()).isPresent()) {
      return ResponseEntity.badRequest().body("Username is already taken");
    }

    if (userService.findByEmail(userDTO.getEmail()).isPresent()) {
      return ResponseEntity.badRequest().body("Email is already taken");
    }

    UserEntity user = new UserEntity();
    user.setUsername(userDTO.getUsername());
    user.setPassword(userDTO.getPassword());
    user.setEmail(userDTO.getEmail());
    user.setBirthdate(userDTO.getBirthdate());
    user.setRoles(userService.getDefaultRoles());

    userService.saveUser(user);

    return ResponseEntity.ok("User registered successfully");
  }
}
