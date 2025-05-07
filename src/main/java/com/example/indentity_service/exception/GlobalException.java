package com.example.indentity_service.exception;

import com.example.indentity_service.dto.ApiResponse;
import jakarta.persistence.Column;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalException {
//    @ExceptionHandler(value = Exception.class)
//    ResponseEntity<ApiResponse> runtimeExceptionHandler(Exception e) {
//
//        ApiResponse apiResponse = new ApiResponse();
//        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getErrorCode());
//        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getErrorMessage());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
//    }


    @ExceptionHandler(value = NoSuchElementException.class)
    ResponseEntity<ApiResponse> runtimeExceptionHandler(NoSuchElementException e) {
        ErrorCode   errorCode = ErrorCode.USER_NOT_FOUND;
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.USER_NOT_FOUND.getErrorCode());
        apiResponse.setMessage(ErrorCode.USER_NOT_FOUND.getErrorMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> accessDeniedException(AccessDeniedException e) {
        ErrorCode   errorCode = ErrorCode.UNAUTHORIZED;
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNAUTHORIZED.getErrorCode());
        apiResponse.setMessage(ErrorCode.UNAUTHORIZED.getErrorMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> appExceptionHandler(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getErrorCode());
        apiResponse.setMessage(errorCode.getErrorMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException e) {
        String enumKey = e.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        try {
            errorCode = ErrorCode.valueOf(enumKey);

        } catch (IllegalArgumentException iae) {

        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getErrorCode());
        apiResponse.setMessage(errorCode.getErrorMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }


}
