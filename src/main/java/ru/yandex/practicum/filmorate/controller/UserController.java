package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private HashMap<Integer, User> users = new HashMap<>();
    private int id = 0;
    @PostMapping()
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        users.put(++id, user);
        user.setId(id);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        log.info("user created '{}'", user);
        return ResponseEntity.ok(user);
    }

    @PutMapping()
    public ResponseEntity<User> update(@Valid @RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
            users.put(user.getId(), user);
            log.info("user updated '{}'", user);
            return ResponseEntity.ok(user);
        } else {
            throw new ValidationException("unknown user id");
        }

    }

    //в этом случае ValidationException будет обрабатываться в @RestControllerAdvice
    //ApplicationExceptionsHandler
    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers() throws ValidationException {
        if (users.isEmpty()) {
            throw new ValidationException("не один пользователь не зарегистрирован в базе");
        }
        log.info("users size '{}'", users.size());
        return ResponseEntity.ok(users.values().stream().collect(Collectors.toList()));
    }

}
