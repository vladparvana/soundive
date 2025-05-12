package com.soundive.user_service.repository;

import com.soundive.user_service.entity.User;
import com.soundive.common.repository.AuditableRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends AuditableRepository<User, String> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}