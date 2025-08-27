package com.manuel.chat.chatwebsockets.controller;

import com.manuel.chat.chatwebsockets.dto.contact.ContactRequestDto;
import com.manuel.chat.chatwebsockets.dto.user.UserResponseDto;
import com.manuel.chat.chatwebsockets.model.User;
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

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@Tag(name = "Contact", description = "Contact operations")
public class ContactController {

    @Autowired
    private UserService userService;


    @Operation(
            summary = "Get Contacts",
            description = "Retrieve the list of contacts for the authenticated user",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contacts retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Missing or invalid JWT token"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<?> getContacts() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userService.findUserByUsernameWithContacts(username);

        List<UserResponseDto> contacts = currentUser.getContactos().stream().map(UserResponseDto::new).toList();

        return ResponseEntity.ok().body(contacts);
    }

    @Operation(
            summary = "Add Contact",
            description = "Add a new contact for the authenticated user",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contact added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or contact already exists"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Missing or invalid JWT token"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> addContact(@Valid @RequestBody ContactRequestDto addContactDto) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        //! Comprobamos que no se agregue a si mismo
        if (username.equals(addContactDto.getUsername())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "You can't agree yourself!"));

        //! Cogemos los usuarios
        User currentUser = userService.findUserByUsernameWithContacts(username);
        User newContact = userService.findUserByUsername(addContactDto.getUsername());

        //! Comprobamos que no lo tenga añadido
        if ( currentUser.getContactos().contains(newContact)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "You can't agree a contact already exists!"));

        //! Añadimos y actualizamos al user
        currentUser.getContactos().add(newContact);
        userService.saveUser(currentUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("message", "Contact added successfully"));
    }

    @Operation(
            summary = "Remove Contact",
            description = "Remove an existing contact from the authenticated user's contacts list",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contact removed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Missing or invalid JWT token"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Contact not found or could not be removed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/remove")
    public ResponseEntity<?> deleteContact(@Valid @RequestBody ContactRequestDto removeContactDto) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (! userService.deleteContact(username, removeContactDto.getUsername())) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Contact not removed successfully"));

        return ResponseEntity.ok().body(Collections.singletonMap("message", "Contact removed successfully"));
    }
}