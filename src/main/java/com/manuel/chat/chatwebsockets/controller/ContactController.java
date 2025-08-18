package com.manuel.chat.chatwebsockets.controller;

import com.manuel.chat.chatwebsockets.dto.contact.ContactRequestDto;
import com.manuel.chat.chatwebsockets.dto.user.UserResponseDto;
import com.manuel.chat.chatwebsockets.model.User;
import com.manuel.chat.chatwebsockets.service.UserService;
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
public class ContactController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<?> getContacts() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userService.findUserByUsernameWithContacts(username);

        List<UserResponseDto> contacts = currentUser.getContactos().stream().map(UserResponseDto::new).toList();

        return ResponseEntity.ok().body(contacts);
    }

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

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/remove")
    public ResponseEntity<?> deleteContact(@Valid @RequestBody ContactRequestDto removeContactDto) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (! userService.deleteContact(username, removeContactDto.getUsername())) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Contact not removed successfully"));

        return ResponseEntity.ok().body(Collections.singletonMap("message", "Contact removed successfully"));
    }
}