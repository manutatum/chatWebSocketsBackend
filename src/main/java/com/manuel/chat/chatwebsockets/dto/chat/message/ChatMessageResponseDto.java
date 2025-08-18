package com.manuel.chat.chatwebsockets.dto.chat.message;

import com.manuel.chat.chatwebsockets.model.ChatMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class ChatMessageResponseDto {

    private String username;
    private String message;
    private LocalDateTime timestamp;

    public ChatMessageResponseDto(ChatMessage message) {

        this.username = message.getSender().getUsername();
        this.message = message.getContent();
        this.timestamp = message.getTimestamp();

    }

}
