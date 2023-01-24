package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public boolean addFriend(Integer userId, Integer friendId) {
        User user = get(userId);
        user.getFriends().add(friendId);
        User friend = get(friendId);
        friend.getFriends().add(user.getId());
        return true;
    }

    public boolean deleteFriend(Integer userId, Integer friendId) {
        User user = get(userId);
        User friend = get(friendId);
        user.getFriends().remove(friend);
        friend.getFriends().remove(user);
        return true;
    }

    public List<User> getFriends(Integer userId) {
        List<Integer> friendsId = get(userId).getFriends().stream().collect(Collectors.toList());
        List<User> friends = new ArrayList<>();
        for (int id : friendsId) {
            friends.add(get(id));
        }
        return friends;
    }

    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        List<User> commonFriends = get(otherId)
                .getFriends()
                .stream()
                .filter(i -> get(userId).getFriends().contains(i))
                .map(i -> get(i))
                .collect(Collectors.toList());
        return commonFriends;
    }
}