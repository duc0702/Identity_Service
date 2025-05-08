package com.example.indentity_service.controller;

import com.example.indentity_service.dto.ApiResponse;
import com.example.indentity_service.dto.request.AuthenicationRequest;
import com.example.indentity_service.dto.request.IntrospectRequest;
import com.example.indentity_service.dto.request.LogoutRequest;
import com.example.indentity_service.dto.response.AuthenicationResponse;
import com.example.indentity_service.dto.response.IntrospectResponse;
import com.example.indentity_service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class AuthenicationController {
     AuthenticationService authenicationService;


    @PostMapping("/token")
    public ApiResponse<AuthenicationResponse> authenticate(@RequestBody AuthenicationRequest request) {
        var result = authenicationService.authenticate(request);
        return ApiResponse.<AuthenicationResponse>builder()
                .data(result)
                .build();
    }
    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenicationService.introspectResponse(request);
        return ApiResponse.<IntrospectResponse>builder()
                .data(result)
                .build();
    }
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenicationService.logout(request);
        return ApiResponse.<Void>builder()
                .build();
    }


}
