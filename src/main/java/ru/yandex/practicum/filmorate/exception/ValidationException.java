package ru.yandex.practicum.filmorate.exception;

public class ValidationException extends RuntimeException {
    private int httpCode;

    public ValidationException(String message, int httpCode) {
        super(message);
        this.httpCode = httpCode;
    }

    public int getHttpCode() {
        return httpCode;
    }
}
