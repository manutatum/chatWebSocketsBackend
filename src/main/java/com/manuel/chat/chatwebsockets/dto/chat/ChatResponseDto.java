package com.manuel.chat.chatwebsockets.dto.chat;

import com.manuel.chat.chatwebsockets.dto.user.UserResponseDto;
import com.manuel.chat.chatwebsockets.model.Chat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChatResponseDto {

    private Long id;

    private String name;

    private List<UserResponseDto> participants;

    public ChatResponseDto(Chat chat) {
        this.id = chat.getId();
        this.name = chat.getName();
        this.participants = chat.getParticipants().stream().map(UserResponseDto::new).toList();
    }

}
