package com.manuel.chat.chatwebsockets.controller;

import com.manuel.chat.chatwebsockets.dto.user.UserRequestDto;
import com.manuel.chat.chatwebsockets.dto.user.UserResponseDto;
import com.manuel.chat.chatwebsockets.model.User;
import com.manuel.chat.chatwebsockets.service.JwtService;
import com.manuel.chat.chatwebsockets.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(dto));
    }

    @GetMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String bearerToken) {

        String token = bearerToken.replace("Bearer ", "");

        if (token.isBlank()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (!jwtService.validateToken(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return  ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String bearerToken) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UserResponseDto userDto = new UserResponseDto(userService.findUserByUsername(username));

        return ResponseEntity.ok(userDto);
    }
}
