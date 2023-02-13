package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.memoryService.UserService;
import ru.yandex.practicum.filmorate.service.validateService.CustomValidator;
import ru.yandex.practicum.filmorate.storage.memoryStorage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.storageInterface.UserStorage;

import java.time.LocalDate;

public class UserControllerTest {
    UserController userController;
    CustomValidator validator;
    UserService userService;
    UserStorage storage;

    @BeforeEach
    void init() {
        validator = new CustomValidator<User>();
        storage = new InMemoryUserStorage();
        userService = new UserService(storage);
        userController = new UserController(validator, userService);
    }

    @Test
    @DisplayName("создать пользователя с кривой почтой")
    void createUserBadEmail() {
        User user = new User(0, "qweqweadawd", "login", "name"
                , LocalDate.of(1982, 01, 01));
        final ValidationException ex = Assertions.assertThrows(ValidationException.class,
                () -> validator.validate(user));
        Assertions.assertEquals("bad email", ex.getMessage());
    }

    @Test
    @DisplayName("создать пользователя с пустой почтой")
    void createUserEmptyEmail() {
        User user = new User(0, null, "login", "name"
                , LocalDate.of(1982, 01, 01));
        ValidationException ex = Assertions.assertThrows(ValidationException.class,
                () -> validator.validate(user));
        Assertions.assertEquals("bad email", ex.getMessage());

        user.setEmail(null);
        ex = Assertions.assertThrows(ValidationException.class,
                () -> validator.validate(user));
        Assertions.assertEquals("bad email", ex.getMessage());
    }

    @Test
    @DisplayName("создать пользователя с кривым логином")
    void createUserBadLogin() {
        User user = new User(0, "qweqw@eadawd", "login login", "name"
                , LocalDate.of(1982, 01, 01));
        final ValidationException ex = Assertions.assertThrows(ValidationException.class,
                () -> validator.validate(user));
        Assertions.assertEquals("bad login", ex.getMessage());
    }

    @Test
    @DisplayName("создать пользователя с пустым логином")
    void createUserEmptyLogin() {
        User user = new User(0, "qweqw@eadawd", "", "name"
                , LocalDate.of(1982, 01, 01));
        ValidationException ex = Assertions.assertThrows(ValidationException.class,
                () -> validator.validate(user));
        Assertions.assertEquals("bad login", ex.getMessage());

        user.setLogin(null);
        ex = Assertions.assertThrows(ValidationException.class,
                () -> validator.validate(user));
        Assertions.assertEquals("bad login", ex.getMessage());
    }

    @Test
    @DisplayName("создать пользователя с датой рождения позже текущего времени")
    void createUserBadBirthday() {
        User user = new User(0, "qweqw@eadawd", "login", "name"
                , LocalDate.of(2024, 01, 01));
        final ValidationException ex = Assertions.assertThrows(ValidationException.class,
                () -> validator.validate(user));
        Assertions.assertEquals("bad birthday", ex.getMessage());
    }

}
