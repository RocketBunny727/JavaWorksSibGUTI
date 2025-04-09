package org.example.repository;

import org.example.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAuthUserRepository extends JpaRepository<AuthUser, Long> {
    Optional<AuthUser> findByLogin(String login);
}
