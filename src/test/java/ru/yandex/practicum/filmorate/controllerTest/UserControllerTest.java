package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserControllerTest {
    UserController userController;

    @BeforeEach
    void init() {
        userController = new UserController();
    }

    @Test
    @DisplayName("создать пользователя с кривой почтой")
    void createOkUser() {
        User user = new User(0,"qweqweadawd","login","name", LocalDate.of(1982,01,01));
        final ValidationException ex = Assertions.assertThrows(ValidationException.class,
                ()->userController.validate(user));
        Assertions.assertEquals("bad email",ex.getMessage());
    }
}
