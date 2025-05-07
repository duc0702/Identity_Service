package com.example.indentity_service.service;

import com.example.indentity_service.dto.request.RoleRequest;
import com.example.indentity_service.dto.response.RoleResponse;
import com.example.indentity_service.entity.Permission;
import com.example.indentity_service.entity.Role;
import com.example.indentity_service.mapper.RoleMapper;
import com.example.indentity_service.repository.PermissionRepository;
import com.example.indentity_service.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    PermissionRepository permissionRepository;
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    public RoleResponse createRole(RoleRequest request) {
        Role role = roleMapper.toRole(request);
        // tìm theo list permisson gửi về
        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);

    }

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public void deleteRole(String id) {
        roleRepository.deleteById(id);
    }
}
