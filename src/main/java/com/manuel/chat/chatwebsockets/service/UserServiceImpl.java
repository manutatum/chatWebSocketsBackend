package com.manuel.chat.chatwebsockets.service;

import com.manuel.chat.chatwebsockets.dto.user.UserRequestDto;
import com.manuel.chat.chatwebsockets.dto.user.UserResponseDto;
import com.manuel.chat.chatwebsockets.model.Role;
import com.manuel.chat.chatwebsockets.model.User;
import com.manuel.chat.chatwebsockets.model.enums.ERole;
import com.manuel.chat.chatwebsockets.repository.RoleRepository;
import com.manuel.chat.chatwebsockets.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserResponseDto disabledUser(Long id) {

        User user = userRepository.findById(id).orElseThrow(() ->  new NoSuchElementException("User not found"));

        user.setEnabled(false);

        User userUpdated = userRepository.save(user);

        return new UserResponseDto(userUpdated);
    }

    @Override
    public UserResponseDto enabledUser(Long id) {

        User user = userRepository.findById(id).orElseThrow(() ->  new NoSuchElementException("User not found"));

        user.setEnabled(true);

        User userUpdated = userRepository.save(user);

        return new UserResponseDto(userUpdated);
    }

    @Override
    public boolean deleteContact(String currentUsername, String contactUsername) {

        User currentUser = findUserByUsernameWithContacts(currentUsername);

        User deletedContact = findUserByUsername(contactUsername);

        //! SI NO TIENE A ESE USUARIO
        if (!currentUser.getContactos().contains(deletedContact)) return false;

        //! SI NO ELIMINA AL USUARIO
        if (!currentUser.getContactos().remove(deletedContact)) return false;

        saveUser(currentUser);

        return true;
    }

    @Override
    public UserResponseDto createUser(UserRequestDto dto) {

        //? COMPROBAMOS QUE EL REPAS Y LA PASS SEAN IGUALES, QUE NO EXISTA USUARIO CON ESE NOMBRE
        if ( !dto.getPassword().equals(dto.getRepassword()) ) throw new IllegalArgumentException("Passwords don't match");

        if ( userRepository.existsByUsername(dto.getUsername())) throw new IllegalArgumentException("Username already exists");

        if ( userRepository.existsByEmail(dto.getEmail())) throw new IllegalArgumentException("Email already exists");

        //? COGEMOS EL ROLE
        Role role = roleRepository.findByName(ERole.ROLE_USER).orElseThrow( () -> new IllegalArgumentException("Role not found"));

        User user = new User();

        user.setUsername(dto.getUsername());

        user.setEmail(dto.getEmail());

        //? ENCRIPTAMOS LA CONTRASEÑA
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        user.setRoles(Collections.singletonList(role));

        return new UserResponseDto(userRepository.save(user));

    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @Override
    public User findUserByUsernameWithContacts(String username) {
        return userRepository.findByUsernameWithContacts(username).orElseThrow(() -> new NoSuchElementException("User not found"));
    }
}
