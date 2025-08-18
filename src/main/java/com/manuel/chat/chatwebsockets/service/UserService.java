package com.manuel.chat.chatwebsockets.service;

import com.manuel.chat.chatwebsockets.model.User;
import com.manuel.chat.chatwebsockets.dto.user.UserRequestDto;
import com.manuel.chat.chatwebsockets.dto.user.UserResponseDto;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


public interface UserService {

    UserResponseDto createUser(UserRequestDto dto);

    void saveUser(User user);

    User findUserByUsername(String username);

    User findUserByUsernameWithContacts(String username);

    List<User> findAllUsers();

    UserResponseDto disabledUser(@PathVariable Long id);

    UserResponseDto enabledUser(@PathVariable Long id);

    boolean deleteContact(String currentUsername, @NotBlank String contactUsername);
}
