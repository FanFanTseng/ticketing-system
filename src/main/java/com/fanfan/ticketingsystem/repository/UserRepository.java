package com.fanfan.ticketingsystem.repository;

import com.fanfan.ticketingsystem.model.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Project: ticketingsystem Created by: arten Date: 2024/7/1
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByUsername(String username);
}
