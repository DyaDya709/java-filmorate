package ru.yandex.practicum.filmorate.service.memoryService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.sericeInterface.UserServiceable;
import ru.yandex.practicum.filmorate.storage.storageInterface.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService implements UserServiceable {
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
        if (user.getName().isEmpty()) {
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
        User user = storage.get(id);
        if (user == null) {
            throw new NotFoundException("user not found id=" + id);
        }
        return user;
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
        if (user.getName().isEmpty()) {
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

    public boolean addFriend(Integer userId, Integer friendId) throws NotFoundException {
        User user = get(userId);
        User friend = get(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(user.getId());
        return true;
    }

    public boolean removeFriend(Integer userId, Integer friendId) throws NotFoundException {
        User user = get(userId);
        User friend = get(friendId);
        user.getFriends().removeIf(id -> id.equals(friendId));
        friend.getFriends().removeIf(id -> id.equals(userId));
        return true;
    }

    public List<User> getFriends(Integer userId) throws NotFoundException {
        List<Integer> friendsId = get(userId).getFriends().stream().collect(Collectors.toList());
        List<User> friends = new ArrayList<>();
        for (int id : friendsId) {
            friends.add(get(id));
        }
        return friends;
    }

    public List<User> getCommonFriends(Integer userId, Integer otherId) throws NotFoundException {
        List<User> commonFriends = get(otherId)
                .getFriends()
                .stream()
                .filter(i -> {
                    try {
                        return get(userId).getFriends().contains(i);
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(i -> {
                    try {
                        return get(i);
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        return commonFriends;
    }
}
