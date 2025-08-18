package com.manuel.chat.chatwebsockets.repository;

import com.manuel.chat.chatwebsockets.model.Chat;
import com.manuel.chat.chatwebsockets.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByParticipantsContaining(User user);

    Optional<Chat> findByIdAndParticipants_Id(Long chatId, Long userId);
}
