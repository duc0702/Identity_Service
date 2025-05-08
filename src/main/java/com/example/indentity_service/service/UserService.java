package com.example.indentity_service.service;

import com.example.indentity_service.dto.request.UserCreationRequest;
import com.example.indentity_service.dto.request.UserUpdateRequest;
import com.example.indentity_service.dto.response.UserResponse;
import com.example.indentity_service.entity.User;
import com.example.indentity_service.enums.Role;
import com.example.indentity_service.exception.AppException;
import com.example.indentity_service.exception.ErrorCode;
import com.example.indentity_service.mapper.UserMapper;
import com.example.indentity_service.repository.RoleRepository;
import com.example.indentity_service.repository.UserRepository;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;

    UserMapper userMapper;
    RoleRepository roleRepository;

    public UserResponse createUser(UserCreationRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(request);
        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        System.out.println("✅ User đã được xác thực và có quyền ADMIN");
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUserById(String id) {
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED)));
    }

    public UserResponse updateUser(UserUpdateRequest request, String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        System.out.println("Số lượng role tìm được từ DB: " + roles.size());
        roles.forEach(role -> {
            System.out.println("Role: " + role.getName());
            System.out.println("  Permissions:");
            role.getPermissions().forEach(permission -> {
                System.out.println("    - " + permission.getName());
            });
        });
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
        userRepository.deleteById(id);
    }

    public UserResponse getInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

}
