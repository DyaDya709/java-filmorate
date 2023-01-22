package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserService implements Serviceable<User> {
    private final UserStorage storage;
    private int id = 0;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    private void generateId(final User user) {
        user.setId(++id);
    }

    @Override
    public void create(User user) {
        generateId(user);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        storage.put(user.getId(), user);
        log.info("user created '{}'", user);
    }

    @Override
    public void add(User user) {
        storage.put(user.getId(), user);
    }

    @Override
    public User get(Integer id) {
        return storage.get(id);
    }

    @Override
    public List<User> get() {
        log.info("users size '{}'", storage.size());
        return storage.get();
    }

    @Override
    public void upDate(User user) throws NotFoundException {
        User oldUser = get(user.getId());
        if (oldUser == null) {
            String text = String.format("user id %d not found", user.getId());
            throw new NotFoundException(text);
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        remove(oldUser.getId());
        add(user);
        log.info("user updated '{}'", user);
    }

    @Override
    public void remove(Integer id) {
        storage.remove(id);
    }

    public void addFriend(User user, User friend) {
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
    }

    public void addFriend(User user, Integer friendId) {
        user.getFriends().add(friendId);
        User friend = storage.get(friendId);
        if (friend != null) {
            friend.getFriends().add(user.getId());
        }
    }
}
