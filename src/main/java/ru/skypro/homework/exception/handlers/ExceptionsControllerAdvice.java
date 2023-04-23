package ru.skypro.homework.exception.handlers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.skypro.homework.exception.BadRequestException;
import ru.skypro.homework.exception.ConflictException;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.exception.NotFoundException;

@ControllerAdvice
public class ExceptionsControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFound() {
        return ResponseEntity.status(404).build();
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> forbidden() {
        return ResponseEntity.status(403).build();
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> badParam() {
        return ResponseEntity.status(400).build();
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> conflict() {
        return ResponseEntity.status(409).build();
    }
}
