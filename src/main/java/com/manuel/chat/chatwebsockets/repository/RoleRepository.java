package com.manuel.chat.chatwebsockets.repository;

import com.manuel.chat.chatwebsockets.model.Role;
import com.manuel.chat.chatwebsockets.model.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
