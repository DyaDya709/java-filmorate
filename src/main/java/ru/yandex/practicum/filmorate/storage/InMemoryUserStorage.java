package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> data = new HashMap<>();

    @Override
    public void put(Integer id, User user) {
        data.put(id, user);
    }

    @Override
    public void put(User user) {

    }

    @Override
    public void upDate(User user) {

    }

    @Override
    public User get(Integer id) {
        return data.get(id);
    }

    @Override
    public User get(String email) {
        return null;
    }

    @Override
    public List<User> get() {
        return data.values().stream().collect(Collectors.toList());
    }

    @Override
    public void remove(Integer id) {
        data.remove(id);
    }

    @Override
    public void remove(User user) {
        data.remove(user.getId());
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public int getMaxId() {
        return Collections.max(data.keySet());
    }

    @Override
    public void addFriend(User user, User friend) {

    }

    @Override
    public boolean removeFriend(Integer userId, Integer friendId) {
        return false;
    }
}
