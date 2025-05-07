package com.example.indentity_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RoleResponse {
    String name;
    String description;
    Set<PermissionResponse> permissions;
}
