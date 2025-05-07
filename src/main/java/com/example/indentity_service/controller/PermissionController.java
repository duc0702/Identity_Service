package com.example.indentity_service.controller;

import com.example.indentity_service.dto.ApiResponse;
import com.example.indentity_service.dto.request.PermissionRequest;
import com.example.indentity_service.dto.response.PermissionResponse;
import com.example.indentity_service.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping("")
    public ApiResponse<PermissionResponse> createPermission( @RequestBody PermissionRequest request){
        return  ApiResponse.<PermissionResponse>builder()
                .data(permissionService.createPermission(request))
                .build();
    }
    @GetMapping("")
    public ApiResponse<List<PermissionResponse>> getAllPermission(){
        return ApiResponse.<List<PermissionResponse>>builder()
                .data(permissionService.getAllPermissions())
                .build();
    }
    @DeleteMapping("{permissionId}")
    public ApiResponse<Void> deletePermission(@PathVariable("permissionId") String id){
        permissionService.deletePermission(id);
        return ApiResponse.<Void>builder().build();
    }

}
