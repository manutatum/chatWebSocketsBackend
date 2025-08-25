package com.manuel.chat.chatwebsockets.repository;

import com.manuel.chat.chatwebsockets.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
