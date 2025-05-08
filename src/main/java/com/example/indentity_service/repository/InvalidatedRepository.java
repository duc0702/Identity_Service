package com.example.indentity_service.repository;

import com.example.indentity_service.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidatedRepository extends JpaRepository<InvalidatedToken, String> {
}
