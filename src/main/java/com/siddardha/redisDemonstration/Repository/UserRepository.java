package com.siddardha.redisDemonstration.Repository;

import com.siddardha.redisDemonstration.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String userName);

    boolean existsByUsername(String username);
}
