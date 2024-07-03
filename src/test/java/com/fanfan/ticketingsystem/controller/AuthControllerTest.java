package com.fanfan.ticketingsystem.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fanfan.ticketingsystem.TicketingsystemApplication;
import com.fanfan.ticketingsystem.dto.AuthenticationRequest;
import com.fanfan.ticketingsystem.dto.UserDTO;
import com.fanfan.ticketingsystem.model.Role;
import com.fanfan.ticketingsystem.model.UserEntity;
import com.fanfan.ticketingsystem.service.UserService;
import com.fanfan.ticketingsystem.util.JwtUtil;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest(classes = TicketingsystemApplication.class)
public class AuthControllerTest {

  private MockMvc mockMvc;

  @MockBean
  private AuthenticationManager authenticationManager;

  @MockBean
  private UserService userService;

  @MockBean
  private JwtUtil jwtUtil;

  @InjectMocks
  private AuthController authController;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
  }

  @Test
  public void testLoginSuccess() throws Exception {
    AuthenticationRequest request = new AuthenticationRequest();
    request.setUsername("user");
    request.setPassword("password");

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(new UsernamePasswordAuthenticationToken("user", "password"));

    UserDetails userDetails = org.springframework.security.core.userdetails.User
        .withUsername("user")
        .password("password")
        .authorities("ROLE_USER")
        .build();

    when(userService.loadUserByUsername(anyString())).thenReturn(userDetails);
    when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("dummy-jwt-token");

    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\": \"user\", \"password\": \"password\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.jwt", is("dummy-jwt-token")));
  }

  @Test
  public void testLoginFailure() throws Exception {
    AuthenticationRequest request = new AuthenticationRequest();
    request.setUsername("user");
    request.setPassword("wrongpassword");

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new RuntimeException("Incorrect username or password"));

    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\": \"user\", \"password\": \"wrongpassword\"}"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void testRegisterSuccess() throws Exception {
    UserDTO userDTO = new UserDTO();
    userDTO.setUsername("newuser");
    userDTO.setPassword("password");
    userDTO.setEmail("newuser@example.com");
    userDTO.setBirthdate(LocalDate.of(1990, 1, 1));

    when(userService.findByUsername(anyString())).thenReturn(Optional.empty());
    when(userService.findByEmail(anyString())).thenReturn(Optional.empty());
    when(userService.saveUser(any(UserEntity.class))).thenReturn(new UserEntity());
    when(userService.getDefaultRoles()).thenReturn(Set.of(new Role("ROLE_USER")));

    mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\": \"newuser\", \"password\": \"password\", \"email\": \"newuser@example.com\", \"birthdate\": \"1990-01-01\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is("User registered successfully")));
  }

  @Test
  public void testRegisterFailure_UsernameTaken() throws Exception {
    UserDTO userDTO = new UserDTO();
    userDTO.setUsername("existinguser");
    userDTO.setPassword("password");
    userDTO.setEmail("newuser@example.com");
    userDTO.setBirthdate(LocalDate.of(1990, 1, 1));

    when(userService.findByUsername(anyString())).thenReturn(Optional.of(new UserEntity()));

    mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\": \"existinguser\", \"password\": \"password\", \"email\": \"newuser@example.com\", \"birthdate\": \"1990-01-01\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", is("Username is already taken")));
  }

  @Test
  public void testRegisterFailure_EmailTaken() throws Exception {
    UserDTO userDTO = new UserDTO();
    userDTO.setUsername("newuser");
    userDTO.setPassword("password");
    userDTO.setEmail("existingemail@example.com");
    userDTO.setBirthdate(LocalDate.of(1990, 1, 1));

    when(userService.findByUsername(anyString())).thenReturn(Optional.empty());
    when(userService.findByEmail(anyString())).thenReturn(Optional.of(new UserEntity()));

    mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\": \"newuser\", \"password\": \"password\", \"email\": \"existingemail@example.com\", \"birthdate\": \"1990-01-01\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$", is("Email is already taken")));
  }
}
