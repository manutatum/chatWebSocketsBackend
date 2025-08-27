package com.manuel.chat.chatwebsockets.controller;

import com.manuel.chat.chatwebsockets.dto.user.UserRequestDto;
import com.manuel.chat.chatwebsockets.dto.user.UserResponseDto;
import com.manuel.chat.chatwebsockets.service.JwtService;
import com.manuel.chat.chatwebsockets.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Authentication operations")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Operation(
            summary = "Register a new User",
            description = "Register a new user already not exists"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters, user already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(dto));
    }

    @Operation(
            summary = "Validate JWT Token",
            description = "Checks if the provided JWT token is valid and not expired"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token is valid"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String bearerToken) {

        String token = bearerToken.replace("Bearer ", "");

        if (token.isBlank()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (!jwtService.validateToken(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return  ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Get User Profile",
            description = "Retrieve the profile information of the authenticated user",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Missing or invalid JWT token"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UserResponseDto userDto = new UserResponseDto(userService.findUserByUsername(username));

        return ResponseEntity.ok(userDto);
    }
}
