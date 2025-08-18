package com.manuel.chat.chatwebsockets.dto.chat;

import com.manuel.chat.chatwebsockets.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ChatParticipantRequestDto {

    private Long id;

    private String username;

    public ChatParticipantRequestDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }

}
