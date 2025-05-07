package com.example.indentity_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public enum ErrorCode {
    USER_EXISTED(1002, "User already existed",HttpStatus.INTERNAL_SERVER_ERROR),
    UNCATEGORIZED_EXCEPTION(9999, "UNCATEGORIZED_EXCEPTION",HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "USERNAME MUST BE AT LEAST 3 characters",HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004,"PASSWORD MUST BE AT LEAST 8 characters",HttpStatus.BAD_REQUEST),
    INVALID_KEY(1001,"Invalid key",HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "USER_NOT_EXISTED",HttpStatus.NOT_FOUND),
    UNAUTHENICATION(1006, "AUTHENICATION",HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND(1007, " USER_NOT_FOUND" ,HttpStatus.NOT_FOUND),
    UNAUTHORIZED(1008, "You do not have permission" ,HttpStatus.FORBIDDEN),
    INVALID_DOB(1009, "Your must be at least {}" ,HttpStatus.BAD_REQUEST),
    ;
    private int errorCode;
    private String errorMessage;
    private HttpStatus httpStatus;

    ErrorCode(int errorCode, String errorMessage,HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
