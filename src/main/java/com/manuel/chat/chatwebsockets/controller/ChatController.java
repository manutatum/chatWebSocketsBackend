package com.manuel.chat.chatwebsockets.controller;

import com.manuel.chat.chatwebsockets.dto.chat.ChatParticipantRequestDto;
import com.manuel.chat.chatwebsockets.dto.chat.ChatRequestDto;
import com.manuel.chat.chatwebsockets.dto.chat.ChatResponseDto;
import com.manuel.chat.chatwebsockets.dto.chat.message.ChatMessageResponseDto;
import com.manuel.chat.chatwebsockets.dto.user.UserResponseDto;
import com.manuel.chat.chatwebsockets.model.User;
import com.manuel.chat.chatwebsockets.service.ChatService;
import com.manuel.chat.chatwebsockets.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<?> getChats() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userService.findUserByUsername(username);

        List<ChatResponseDto> chats = chatService.getChatsForUser(currentUser);

        return ResponseEntity.ok().body(chats);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createChat(@RequestBody @Valid ChatRequestDto chatRequestDto ) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        chatRequestDto.getParticipants().add(new ChatParticipantRequestDto(userService.findUserByUsername(username)));

        ChatResponseDto response = chatService.createChat(chatRequestDto);

        if (response == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(Collections.singletonMap("chat", response));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{chatId}/messages")
    public ResponseEntity<?> getChatMessages(@PathVariable Long chatId) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userService.findUserByUsername(username);

        List<ChatMessageResponseDto> messages = chatService.getMessagesForChat(new UserResponseDto(currentUser), chatId);

        return ResponseEntity.ok(messages);

    }
}
