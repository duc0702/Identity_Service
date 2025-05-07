package com.example.indentity_service.service;

import com.example.indentity_service.dto.request.PermissionRequest;
import com.example.indentity_service.dto.response.PermissionResponse;
import com.example.indentity_service.entity.Permission;
import com.example.indentity_service.mapper.PermissionMapper;
import com.example.indentity_service.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;
   public PermissionResponse createPermission(PermissionRequest request) {
       System.out.println("request: " + request.getName() + ", " + request.getDescription());
        Permission permission = permissionMapper.toPermission(request);
       System.out.println("Mapped Permission: " + permission.getName() + ", " + permission.getDescription());
         permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }
    public List<PermissionResponse> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toPermissionResponse)
                .toList();
    }
    public void deletePermission(String id) {
        permissionRepository.deleteById(id);
    }

}
