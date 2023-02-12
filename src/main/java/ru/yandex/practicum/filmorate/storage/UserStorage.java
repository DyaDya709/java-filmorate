package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserStorage {
    void put(Integer id, User user);

    User get(Integer id);

    List<User> get();

    void remove(Integer id);

    void remove(User user);

    int size();
}
