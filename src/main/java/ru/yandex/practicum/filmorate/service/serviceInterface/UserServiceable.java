package ru.yandex.practicum.filmorate.service.serviceInterface;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserServiceable {
    void create(User element);

    User get(Integer id) throws NotFoundException;

    List<User> get();

    void upDate(User element) throws NotFoundException;

    void remove(Integer id);

    boolean addFriend(Integer userId, Integer friendId) throws NotFoundException;

    boolean removeFriend(Integer userId, Integer friendId) throws NotFoundException;

    List<User> getFriends(Integer userId) throws NotFoundException;

    List<User> getCommonFriends(Integer userId, Integer otherId) throws NotFoundException;
}
