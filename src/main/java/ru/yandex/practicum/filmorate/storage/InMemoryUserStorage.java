package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

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
    public User get(Integer id) {
        return data.get(id);
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
}
