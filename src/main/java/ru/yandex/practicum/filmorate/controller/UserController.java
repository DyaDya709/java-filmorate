package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.CustomValidator;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User> {
    private CustomValidator<User> validator;
    private UserService userService;

    @Autowired
    public UserController(CustomValidator<User> validator, UserService userService) {
        this.validator = validator;
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        validator.validate(user);
        userService.create(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping()
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        validator.validate(user);
        userService.upDate(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping()
    ResponseEntity<List<User>> getAllElements() {
        return ResponseEntity.ok(userService.get());
    }

    @GetMapping("/{userId}")
    ResponseEntity<User> get(@PathVariable(required = false, value = "userId") Integer id) {
        if (id != null) {
            return ResponseEntity.ok(userService.get(id));
        } else {
            throw new BadRequestException("user id missing");
        }
    }

    @PutMapping("/{id}/friends/{friendId}")
    ResponseEntity<Boolean> addFiend(@PathVariable Integer id, @PathVariable Integer friendId) {
        return ResponseEntity.ok(userService.addFriend(id, friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    ResponseEntity<Boolean> removeFiend(@PathVariable Integer id, @PathVariable Integer friendId) {
        return ResponseEntity.ok(userService.removeFriend(id, friendId));
    }

    @GetMapping("/{id}/friends")
    ResponseEntity<List<User>> getFriends(@PathVariable(required = false) Integer id) {
        if (id != null) {
            return ResponseEntity.ok(userService.getFriends(id));
        } else {
            throw new NotFoundException("user id missing");
        }
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    ResponseEntity<List<User>> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        if (id != null && otherId != null) {
            return ResponseEntity.ok(userService.getCommonFriends(id, otherId));
        } else {
            throw new NotFoundException("user id or other id missing");
        }
    }
}
