package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserStorage {
    void put(Integer id, User user);

    void put(User user);

    void upDate(User user);

    User get(Integer id);

    User get(String email);

    List<User> get();

    void remove(Integer id);

    void remove(User user);

    int size();

    int getMaxId();

    void addFriend(User user, User friend);

    boolean removeFriend(Integer userId, Integer friendId);
}
