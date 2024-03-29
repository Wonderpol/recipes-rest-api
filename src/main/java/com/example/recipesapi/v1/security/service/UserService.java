package com.example.recipesapi.v1.security.service;

import com.example.recipesapi.v1.security.exception.UserAlreadyExistsException;
import com.example.recipesapi.v1.security.exception.UserNotFoundException;
import com.example.recipesapi.v1.security.model.CustomUserDetails;
import com.example.recipesapi.v1.security.model.dto.UserDto;
import com.example.recipesapi.v1.security.model.dto.UserMapper;
import com.example.recipesapi.v1.security.model.entity.User;
import com.example.recipesapi.v1.security.model.request.AuthenticationRequest;
import com.example.recipesapi.v1.security.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public UserService(final UserRepository userRepository, final PasswordEncoder passwordEncoder, final UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) {
        final User user = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new UserNotFoundException("User with email: " + email + " not found")
                );
        return new CustomUserDetails(user);
    }

    public UserDto registerUser(AuthenticationRequest user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(u -> {
                    throw new UserAlreadyExistsException("User with email: " + u.getEmail() + " already exists");
                });
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        final User savedUser = userRepository.saveAndFlush(new User(user.getEmail(), user.getPassword()));

        return userMapper.convertToDto(savedUser);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with id: " + id + " not found")
        );
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User with email: " + email + " does not exists")
        );
    }

}
