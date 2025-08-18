package com.manuel.chat.chatwebsockets.dto.contact;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactRequestDto {

    @NotBlank
    private String username;

}
