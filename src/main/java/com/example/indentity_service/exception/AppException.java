package com.example.indentity_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class AppException extends RuntimeException{
     private ErrorCode errorCode;

     public AppException(ErrorCode errorCode) {
          super(errorCode.getErrorMessage());
          this.errorCode = errorCode;
     }
}
