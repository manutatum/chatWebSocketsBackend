package com.manuel.chat.chatwebsockets.repository;

import com.manuel.chat.chatwebsockets.model.ChatMessage;
import com.manuel.chat.chatwebsockets.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
