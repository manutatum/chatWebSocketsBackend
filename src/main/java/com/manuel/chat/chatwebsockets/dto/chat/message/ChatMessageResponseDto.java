package com.manuel.chat.chatwebsockets.dto.chat.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.manuel.chat.chatwebsockets.model.ChatMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@NoArgsConstructor
@Getter
@Setter
public class ChatMessageResponseDto {

    private Long chatId;
    private String username;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private OffsetDateTime timestamp;

    public ChatMessageResponseDto(ChatMessage message) {

        this.chatId = message.getChat().getId();
        this.username = message.getSender().getUsername();
        this.message = message.getContent();
        this.timestamp = message.getTimestamp();

    }

}
