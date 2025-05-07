package com.example.indentity_service.mapper;

import com.example.indentity_service.dto.request.PermissionRequest;
import com.example.indentity_service.dto.response.PermissionResponse;
import com.example.indentity_service.entity.Permission;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
