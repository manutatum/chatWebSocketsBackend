package com.manuel.chat.chatwebsockets.repository;

import com.manuel.chat.chatwebsockets.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.contactos WHERE u.username = :username")
    Optional<User> findByUsernameWithContacts(@Param("username") String username);

}
