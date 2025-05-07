package com.example.indentity_service.controller;

import com.example.indentity_service.dto.ApiResponse;
import com.example.indentity_service.dto.request.UserCreationRequest;
import com.example.indentity_service.dto.request.UserUpdateRequest;
import com.example.indentity_service.dto.response.UserResponse;
import com.example.indentity_service.entity.User;
import com.example.indentity_service.mapper.UserMapper;
import com.example.indentity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    private UserService userService;
    private final UserMapper userMapper;

    @PostMapping("")
    public ApiResponse createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Thêm người dùng thành công");
        apiResponse.setData(userService.createUser(request));
        return apiResponse;
    }


    @GetMapping("")
    public ApiResponse<List<UserResponse>> getAllUsers() {
        var authenication = SecurityContextHolder.getContext().getAuthentication();
        log.info("User name: {}", authenication.getName());
        authenication.getAuthorities().forEach(grantedAuthority -> log.info( grantedAuthority.getAuthority()));
        ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Lấy danh sách người dùng thành công");
        apiResponse.setData(userService.getAllUsers());
        return apiResponse;
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUser(@PathVariable("userId") String id) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Lấy thông tin người dùng thành công");
        apiResponse.setData(userService.getUserById(id));
        return apiResponse;

    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(@RequestBody UserUpdateRequest request, @PathVariable("userId") String id) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Update thông tin người dùng thành công");
        apiResponse.setData(userService.updateUser(request, id));
        return apiResponse;
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<String> deleteUser(@PathVariable("userId") String id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        userService.deleteUser(id);
        apiResponse.setMessage("Xóa  người dùng thành công");
        return apiResponse;
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> myInfo(){
        return ApiResponse.<UserResponse>builder()
                .data(userService.getInfo())
                .build();
    }
}
