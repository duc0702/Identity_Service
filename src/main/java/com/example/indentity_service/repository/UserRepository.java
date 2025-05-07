package com.example.indentity_service.repository;

import com.example.indentity_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Boolean existsByUsername(String userName);
    Optional<User> findByUsername(String userName);
}
