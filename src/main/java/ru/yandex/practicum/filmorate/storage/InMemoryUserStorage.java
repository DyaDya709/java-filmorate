package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> DATA = new HashMap<>();

    @Override
    public void put(Integer id, User user) {
        DATA.put(id, user);
    }

    @Override
    public User get(Integer id) {
        return DATA.get(id);
    }

    @Override
    public List<User> get() {
        return DATA.values().stream().collect(Collectors.toList());
    }

    @Override
    public void remove(Integer id) {
        DATA.remove(id);
    }

    @Override
    public void remove(User user) {
        DATA.remove(user.getId());
    }

    @Override
    public int size() {
        return DATA.size();
    }
}
