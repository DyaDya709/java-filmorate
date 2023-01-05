package ru.yandex.practicum.filmorate.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionsHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleInvalidArgument(MethodArgumentNotValidException e) {
        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        String objectName = e.getBindingResult().getObjectName();
        int httpCode = 404;
        switch (objectName) {
            case "film":
                if (errorMap.containsKey("name") || errorMap.containsKey("duration")) {
                    httpCode = 400;
                }
                break;
            case "user":
                if (errorMap.containsKey("login") || errorMap.containsKey("birthday")) {
                    httpCode = 400;
                }
                break;
        }
        return new ResponseEntity<>(errorMap, HttpStatus.resolve(httpCode));
    }


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(ValidationException e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", e.getMessage());
        return new ResponseEntity<>(errorMap, HttpStatus.resolve(e.getHttpCode()));
    }

    //Вариант без создания ResponseEntity
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @ExceptionHandler(ValidationException.class)
//    public Map<String, String> handleValidationException(ValidationException e) {
//        Map<String, String> errorMap = new HashMap<>();
//        errorMap.put("error", e.getMessage());
//        return errorMap;
//    }
}
