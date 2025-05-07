package com.example.indentity_service.controller;

import com.example.indentity_service.dto.ApiResponse;
import com.example.indentity_service.dto.request.PermissionRequest;
import com.example.indentity_service.dto.request.RoleRequest;
import com.example.indentity_service.dto.response.PermissionResponse;
import com.example.indentity_service.dto.response.RoleResponse;
import com.example.indentity_service.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
        RoleService roleService;

    @PostMapping("")
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest request){
        return  ApiResponse.<RoleResponse>builder()
                .data(roleService.createRole(request))
                .build();
    }
    @GetMapping("")
    public ApiResponse<List<RoleResponse>> getAllRole(){
        return ApiResponse.<List<RoleResponse>>builder()
                .data(roleService.getAllRoles())
                .build();
    }
    @DeleteMapping("{roleId}")
    public ApiResponse<Void> deleteRole(@PathVariable("roleId") String id){
        roleService.deleteRole(id);
        return ApiResponse.<Void>builder().build();
    }
}
