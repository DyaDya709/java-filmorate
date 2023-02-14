package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.dbService.UserDbService;
import ru.yandex.practicum.filmorate.service.sericeInterface.UserServiceable;
import ru.yandex.practicum.filmorate.service.validateService.CustomValidator;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User> {
    private CustomValidator<User> validator;
    private UserServiceable userServiceable;

    @Autowired
    public UserController(CustomValidator<User> validator, UserServiceable userServiceable) {
        this.validator = validator;
        this.userServiceable = userServiceable;
    }

    @PostMapping()
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        validator.validate(user);
        userServiceable.create(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping()
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        validator.validate(user);
        userServiceable.upDate(user);
        return ResponseEntity.ok(userServiceable.get(user.getId()));
    }

    @GetMapping()
    ResponseEntity<List<User>> getAllElements() {
        return ResponseEntity.ok(userServiceable.get());
    }

    @GetMapping("/{userId}")
    ResponseEntity<User> get(@PathVariable(required = false, value = "userId") Integer id) {
        if (id != null) {
            return ResponseEntity.ok(userServiceable.get(id));
        } else {
            throw new BadRequestException("user id missing");
        }
    }

    @PutMapping("/{id}/friends/{friendId}")
    ResponseEntity<Boolean> addFiend(@PathVariable Integer id, @PathVariable Integer friendId) {
        return ResponseEntity.ok(userServiceable.addFriend(id, friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    ResponseEntity<Boolean> removeFiend(@PathVariable Integer id, @PathVariable Integer friendId) {
        return ResponseEntity.ok(userServiceable.removeFriend(id, friendId));
    }

    @GetMapping("/{id}/friends")
    ResponseEntity<List<User>> getFriends(@PathVariable(required = false) Integer id) {
        if (id != null) {
            return ResponseEntity.ok(userServiceable.getFriends(id));
        } else {
            throw new NotFoundException("user id missing");
        }
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    ResponseEntity<List<User>> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        if (id != null && otherId != null) {
            return ResponseEntity.ok(userServiceable.getCommonFriends(id, otherId));
        } else {
            throw new NotFoundException("user id or other id missing");
        }
    }
}
