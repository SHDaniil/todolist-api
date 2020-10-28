package com.nitinol.todolistapi.exceptions;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер исключений
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BigLengthException.class)
    ResponseEntity<String> handleBigLengthException() {
        return new ResponseEntity<String>(
                "Длина должна быть меньше 250 символов",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    ResponseEntity<String> handleConflictException() {
        return new ResponseEntity<String>(
                "Объект с такими параметрами уже существует",
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<String> handleNotFoundException() {
        return new ResponseEntity<String>(
                "Объект с таким идентификатором не существует",
                HttpStatus.NOT_FOUND);
    }
}
