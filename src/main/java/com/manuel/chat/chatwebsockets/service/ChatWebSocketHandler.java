package com.manuel.chat.chatwebsockets.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.manuel.chat.chatwebsockets.dto.chat.message.ChatMessageRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<Long, Set<WebSocketSession>> chatRooms = new ConcurrentHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private ChatService chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("Cliente conectado: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        //! Mapeo el mensaje recibido a nuestro DTO
        ChatMessageRequestDto msgDto = mapper.readValue(message.getPayload(), ChatMessageRequestDto.class);

        String type = msgDto.getType();
        Long chatId = msgDto.getChatId();

        System.out.println("Tipo: " + type);

        //! UNIRSE AL CHAT
        if ("join".equals(type)) {
            chatRooms.computeIfAbsent(chatId, k -> Collections.synchronizedSet(Collections.newSetFromMap(new ConcurrentHashMap<>())))
                    .add(session);
            System.out.println("Sesion " + session.getId() + " se unió a chat " + chatId);
            return;
        }

        //! SALIRSE DEL CHAT
        if ("leave".equals(type)) {
            Set<WebSocketSession> room = chatRooms.get(chatId);
            if (room != null) room.remove(session);
            System.out.println("Sesion " + session.getId() + " salió de chat " + chatId);
            return;
        }

        //! MANDAR MENSAJE
        if ("message".equals(type)) {
            Set<WebSocketSession> room = chatRooms.get(chatId);

            if (room != null) {

                //! CONVERTIMOS EL DTO EN UN MENSAJE QUE SE VA A ENVIAR A LOS USUARIOS
                TextMessage outMsg = new TextMessage(mapper.writeValueAsString(msgDto));

                synchronized (room) {

                    //! GUARDAR MENSAJE EN LA BD
                    chatService.saveMessage(msgDto);

                    //! ENVIAMOS A CADA SESIÓN QUE ESTÉ ABIERTA EL MENSAJE
                    for (WebSocketSession s : room) {
                        if (s.isOpen()) s.sendMessage(outMsg);
                    }
                }

            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        System.out.println("Cliente desconectado: " + session.getRemoteAddress());
        // Limpiamos sesión de todas las salas
        chatRooms.values().forEach(set -> set.remove(session));
    }
}
