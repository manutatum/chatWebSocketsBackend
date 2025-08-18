package com.manuel.chat.chatwebsockets.service;

import com.manuel.chat.chatwebsockets.dto.chat.ChatRequestDto;
import com.manuel.chat.chatwebsockets.dto.chat.ChatResponseDto;
import com.manuel.chat.chatwebsockets.dto.chat.message.ChatMessageResponseDto;
import com.manuel.chat.chatwebsockets.dto.user.UserResponseDto;
import com.manuel.chat.chatwebsockets.model.Chat;
import com.manuel.chat.chatwebsockets.model.User;
import com.manuel.chat.chatwebsockets.repository.ChatRepository;
import com.manuel.chat.chatwebsockets.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ChatServiceImpl implements ChatService{

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public List<ChatResponseDto> getChatsForUser(User user) {
        List<Chat> chats = chatRepository.findByParticipantsContaining(user);

        return chats.stream().map(ChatResponseDto::new).toList();
    }

    @Override
    public ChatResponseDto createChat(ChatRequestDto chatRequestDto) {

        Chat chat = new Chat();

        chat.setName(chatRequestDto.getName());

        chat.setParticipants(
                chatRequestDto.getParticipants().stream()
                        .map(p -> userRepository.findById(p.getId())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + p.getId()))
                        )
                        .toList()
        );

        return new ChatResponseDto(chatRepository.save(chat));
    }

    @Override
    public List<ChatMessageResponseDto> getMessagesForChat(Long chatId) {

        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new NoSuchElementException("Chat not found"));

        return chat.getMessages().stream().map(ChatMessageResponseDto::new).toList();
    }


}
