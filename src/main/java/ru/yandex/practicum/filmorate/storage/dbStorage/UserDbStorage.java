package ru.yandex.practicum.filmorate.storage.dbStorage;

import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.storageInterface.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private User makeUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setEmail(rs.getString("email"));
        user.setName(rs.getString("name"));
        user.setLogin(rs.getString("login"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());
        setFriendship(user);
        return user;
    }

    private HashMap<Integer, Boolean> getFriends(Integer userId) {
        HashMap<Integer, Boolean> friendship = new HashMap<>();
        try {
            SqlRowSet friendsRows = jdbcTemplate.queryForRowSet("select friend_id from FRIENDS where user_id=?"
                    , userId);
            while (friendsRows.next()) {
                Integer friend_id = friendsRows.getInt("friend_id");
                Boolean confirmed = friendsRows.getBoolean(1);
                friendship.put(friend_id, confirmed);
            }
            return friendship;
        } catch (DataAccessException e) {
            return friendship;
        }
    }

    private void setFriendship(User user) {
        user.setFriendship(getFriends(user.getId()));
        user.setFriends(user.getFriendship()
                .entrySet()
                .stream()
                .map((kv) -> kv.getKey())
                .collect(Collectors.toSet()));
    }

    @Override
    public void put(Integer id, User user) {

    }

    @Override
    public void put(User user) {
        jdbcTemplate.update("insert into USERS(email, login, name, birthday) " +
                "VALUES (?,?,?,?)",
                 user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());

    }

    @Override
    public void upDate(User user) {
        jdbcTemplate.update("update USERS SET email=?, login=?, name=?, birthday=?",
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
    }

    @Override
    public void addFriend(User user, User friend) {
        jdbcTemplate.update("insert into FRIENDS(user_id,friend_id,confirmed) VALUES (?,?,?)"
                , user.getId(),friend.getId(),false);
    }

    public boolean removeFriend(Integer userId, Integer friendId) {
        jdbcTemplate.update("delete FROM FRIENDS where USER_ID = ? AND FRIEND_ID = ?",userId,friendId);
        return true;
    }
    @Override
    public User get(Integer id) {
        try {
            SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from USERS where USER_ID = ?", id);
            if (userRows.next()) {
                String email = userRows.getString("email");
                String name = userRows.getString("name");
                String login = userRows.getString("login");
                LocalDate birthday = userRows.getDate("birthday").toLocalDate();
                User user = new User(id, email, login, name, birthday);
                setFriendship(user);
                return user;
            }
        } catch (DataAccessException e) {
            return null;
        }
        return null;
    }

    @Override
    public User get(String email) {
        try {
            SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from USERS where EMAIL = ?", email);
            if (userRows.next()) {
                Integer id = userRows.getInt("user_id");
                String name = userRows.getString("name");
                String login = userRows.getString("login");
                LocalDate birthday = userRows.getDate("birthday").toLocalDate();
                User user = new User(id, email, login, name, birthday);
                setFriendship(user);
                return user;
            }
        } catch (DataAccessException e) {
            return null;
        }
        return null;
    }

    @Override
    public List<User> get() {
        String sql = "select * from USERS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public void remove(Integer id) {
        jdbcTemplate.update("delete from users where user_id=?",id);
    }

    @Override
    public void remove(User user) {
        jdbcTemplate.update("delete from users where user_id=?",user.getId());
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public int getMaxId() {
        SqlRowSet rows = jdbcTemplate.queryForRowSet("select MAX(user_id) maxId from USERS");
        if (rows.next()) {
            return rows.getInt("maxId");
        }
        return 0;
    }
}
