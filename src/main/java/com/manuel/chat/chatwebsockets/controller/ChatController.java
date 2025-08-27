package com.manuel.chat.chatwebsockets.controller;

import com.manuel.chat.chatwebsockets.dto.chat.ChatParticipantRequestDto;
import com.manuel.chat.chatwebsockets.dto.chat.ChatRequestDto;
import com.manuel.chat.chatwebsockets.dto.chat.ChatResponseDto;
import com.manuel.chat.chatwebsockets.dto.chat.message.ChatMessageResponseDto;
import com.manuel.chat.chatwebsockets.dto.user.UserResponseDto;
import com.manuel.chat.chatwebsockets.model.User;
import com.manuel.chat.chatwebsockets.service.ChatService;
import com.manuel.chat.chatwebsockets.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Chat", description = "Chat operations")
public class ChatController {

    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService;

    @Operation(
            summary = "Get Chats",
            description = "Retrieve the chats of the authenticated user",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chats retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Missing or invalid JWT token"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<?> getChats() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userService.findUserByUsername(username);

        List<ChatResponseDto> chats = chatService.getChatsForUser(currentUser);

        return ResponseEntity.ok().body(chats);
    }

    @Operation(
            summary = "Create Chat",
            description = "Create a new chat including participants",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or participants list"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Missing or invalid JWT token"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createChat(@RequestBody @Valid ChatRequestDto chatRequestDto ) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        chatRequestDto.getParticipants().add(new ChatParticipantRequestDto(userService.findUserByUsername(username)));

        ChatResponseDto response = chatService.createChat(chatRequestDto);

        if (response == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(Collections.singletonMap("chat", response));
    }

    @Operation(
            summary = "Get Chat Messages",
            description = "Retrieve all messages for a specific chat by chatId",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messages retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Missing or invalid JWT token"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Chat not found or user not participant"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{chatId}/messages")
    public ResponseEntity<?> getChatMessages(@PathVariable Long chatId) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userService.findUserByUsername(username);

        List<ChatMessageResponseDto> messages = chatService.getMessagesForChat(new UserResponseDto(currentUser), chatId);

        return ResponseEntity.ok(messages);

    }
}
