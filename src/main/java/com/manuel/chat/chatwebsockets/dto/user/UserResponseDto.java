package com.manuel.chat.chatwebsockets.dto.user;


import com.manuel.chat.chatwebsockets.model.Role;
import com.manuel.chat.chatwebsockets.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDto {

    private Long id;

    private String username;

    private String email;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

}
