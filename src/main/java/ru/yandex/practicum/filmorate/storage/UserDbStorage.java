package ru.yandex.practicum.filmorate.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

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
        Integer id = rs.getInt("user_id");
        String email = rs.getString("email");
        String name = rs.getString("name");
        String login = rs.getString("login");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        User user = new User(id, email, login, name, birthday);
        user.setFriendship(getFriends(user.getId()));
        user.setFriends(user.getFriendship()
                .entrySet()
                .stream()
                .map((kv) -> kv.getKey())
                .collect(Collectors.toSet()));
        return user;
    }

    private HashMap<Integer, Boolean> getFriends(Integer userId) {
        try {
            SqlRowSet friendsRows = jdbcTemplate.queryForRowSet("select friend_id from FRIENDS where user_id=?", userId);
            HashMap<Integer, Boolean> friendship = new HashMap<>();
            while (friendsRows.next()) {
                Integer friend_id = friendsRows.getInt("friend_id");
                Boolean confirmed = friendsRows.getBoolean("confirmed");
                friendship.put(friend_id, confirmed);
            }
            return friendship;
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public void put(Integer id, User user) {
        jdbcTemplate.update("insert into USERS(user_id, email, login, name, birthday) " +
                "VALUES (?,?,?,?,?)",
                user.getId(), user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
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
                user.setFriendship(getFriends(id));
                user.setFriends(user.getFriendship()
                        .entrySet()
                        .stream()
                        .map((kv) -> kv.getKey())
                        .collect(Collectors.toSet()));
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

    }

    @Override
    public void remove(User user) {

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
