package com.fanfan.ticketingsystem.service;


import com.fanfan.ticketingsystem.model.Role;
import com.fanfan.ticketingsystem.model.UserEntity;
import com.fanfan.ticketingsystem.repository.RoleRepository;
import com.fanfan.ticketingsystem.repository.UserRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public Optional<UserEntity> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public Optional<UserEntity> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public UserEntity saveUser(UserEntity user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  public Set<Role> getDefaultRoles() {
    Set<Role> roles = new HashSet<>();
    Optional<Role> userRole = roleRepository.findByName("ROLE_USER");
    userRole.ifPresent(roles::add);
    return roles;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity userEntity = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    return new User(userEntity.getUsername(), userEntity.getPassword(),
        userEntity.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toSet()));
  }
}

