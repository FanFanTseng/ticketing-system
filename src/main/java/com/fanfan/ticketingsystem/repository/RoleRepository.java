package com.fanfan.ticketingsystem.repository;

import com.fanfan.ticketingsystem.model.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Project: ticketingsystem Created by: arten Date: 2024/7/2
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(String name);
}
