package com.manuel.chat.chatwebsockets.controller;

import com.manuel.chat.chatwebsockets.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.private")
    public void sendPrivateMessage(ChatMessage message) {
//        String recipient = message.getRecipient().getUsername();

//        messagingTemplate.convertAndSendToUser(
//                recipient,
//                "/queue/messages",
//                message
//        );
    }

}
