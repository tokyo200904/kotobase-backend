package kotobase_backend.comom.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                404,
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now(),
                "Not Found"
        );

        return ResponseEntity.status(404).body(error);
    }
}