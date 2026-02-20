package com.example.BlogNet.exception;

import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundExcecption.class)
    public ResponseEntity<Map<String,Object>> handleResourceNotFound(ResourceNotFoundExcecption resourceNotFoundExcecption){
        Map<String,Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("message",resourceNotFoundExcecption.getMessage());
        errorResponse.put("status", HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleValidationException(MethodArgumentNotValidException methodArgumentNotValidException){
        Map<String,Object> errorResponse = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        //extract errors
        List<ObjectError> objErrors = methodArgumentNotValidException.getAllErrors();
        for (ObjectError error : objErrors){
            String fieldname = ((FieldError)error).getField();
            String msg = error.getDefaultMessage();
            errors.put(fieldname,msg);
        }

        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("errors",errors);
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    //Duplicate user exception
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<Map<String,Object>> handleDuplicateUser(DuplicateUserException duplicateUserException){
        Map<String,Object> response = new HashMap<>();

        response.put("timestamp", LocalDateTime.now());
        response.put("message",duplicateUserException.getMessage());
        response.put("status",409);
        response.put("error","Conflict");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    //Authorization
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String,Object>> handleUnauthorizedUser(UnauthorizedException unauthorizedException){
        Map<String,Object> response = new HashMap<>();
        response.put("timestamp",LocalDateTime.now());
        response.put("status",403);
        response.put("error","Forbidden");
        response.put("message",unauthorizedException.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }



    // Handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", 500);
        response.put("error", "Internal Server Error");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
