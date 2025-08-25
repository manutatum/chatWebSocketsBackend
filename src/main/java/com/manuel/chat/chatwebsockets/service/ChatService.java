package com.manuel.chat.chatwebsockets.service;

import com.manuel.chat.chatwebsockets.dto.chat.ChatRequestDto;
import com.manuel.chat.chatwebsockets.dto.chat.ChatResponseDto;
import com.manuel.chat.chatwebsockets.dto.chat.message.ChatMessageRequestDto;
import com.manuel.chat.chatwebsockets.dto.chat.message.ChatMessageResponseDto;
import com.manuel.chat.chatwebsockets.dto.user.UserResponseDto;
import com.manuel.chat.chatwebsockets.model.User;

import java.util.List;

public interface ChatService {

    List<ChatResponseDto> getChatsForUser(User user);

    ChatResponseDto createChat(ChatRequestDto chatRequestDto);

    List<ChatMessageResponseDto> getMessagesForChat(UserResponseDto user, Long chatId);

    void saveMessage(ChatMessageRequestDto chatMessageRequestDto);

}
