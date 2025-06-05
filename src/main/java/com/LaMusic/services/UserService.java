package com.LaMusic.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.LaMusic.dto.CreateUserRequest;
import com.LaMusic.dto.UpdateUserRequest;
import com.LaMusic.dto.UserDTO;
import com.LaMusic.entity.User;
import com.LaMusic.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .filter(user -> !user.isDeleted())
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public User findById(UUID id) {
        User user = userRepository.findById(id)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return user;
    }
    
    public UserDTO findUserDTOById(UUID id) {
    	User user = userRepository.findById(id)
    			.filter(u -> !u.isDeleted())
    			.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    	return toDTO(user);
    }

    public UserDTO create(CreateUserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail_verified(false);
        return toDTO(userRepository.save(user));
    }

    public UserDTO update(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());

        return toDTO(userRepository.save(user));
    }

    public void softDelete(UUID id) {
        User user = userRepository.findById(id)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        user.softDelete();
        userRepository.save(user);
    }

    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setEmailVerified(user.getEmail_verified());
        return dto;
    }
}

