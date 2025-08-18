package com.manuel.chat.chatwebsockets.dto.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatRequestDto {

    @NotBlank
    private String name;

    @NotEmpty
    private List<ChatParticipantRequestDto> participants;
}
