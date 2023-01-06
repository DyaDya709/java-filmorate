package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends AbstractController<User> {
    @PostMapping()
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        data.put(++id, user);
        user.setId(id);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        log.info("user created '{}'", user);
        return ResponseEntity.ok(user);
    }

    @PutMapping()
    public ResponseEntity<User> update(@Valid @RequestBody User user) throws ValidationException {
        if (data.containsKey(user.getId())) {
            data.remove(user.getId());
            data.put(user.getId(), user);
            log.info("user updated '{}'", user);
            return ResponseEntity.ok(user);
        } else {
            throw new ValidationException("unknown user id", 404);
        }
    }

    //в этом случае ValidationException будет обрабатываться в @RestControllerAdvice
    //ApplicationExceptionsHandler
    @GetMapping()
    ResponseEntity<List<User>> getAllElements() throws ValidationException {
        if (data.isEmpty()) {
            throw new ValidationException("не один пользователь не зарегистрирован в базе", 404);
        }
        log.info("users size '{}'", data.size());
        return ResponseEntity.ok(data.values().stream().collect(Collectors.toList()));
    }

    @Override
    public void validate(User element) throws ValidationException {
        if (element.getEmail() == null || !element.getEmail().contains("@")) {
            throw new ValidationException("bad email", 400);
        }
        if (element.getLogin() == null || element.getLogin().contains(" ")) {
            throw new ValidationException("bad login", 400);
        }
        if (element.getBirthday() == null || element.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("bad birthday", 400);
        }
    }
}
