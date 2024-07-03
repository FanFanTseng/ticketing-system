package com.fanfan.ticketingsystem.config;

import com.fanfan.ticketingsystem.model.Role;
import com.fanfan.ticketingsystem.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Project: ticketingsystem Created by: arten Date: 2024/7/2
 */
@Component
public class DataLoader implements CommandLineRunner {

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public void run(String... args) throws Exception {
    if (roleRepository.findByName("ROLE_USER").isEmpty()) {
      Role userRole = new Role();
      userRole.setName("ROLE_USER");
      roleRepository.save(userRole);
    }

    if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
      Role adminRole = new Role();
      adminRole.setName("ROLE_ADMIN");
      roleRepository.save(adminRole);
    }
  }
}
