package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.serviceInterface.FilmServiceable;
import ru.yandex.practicum.filmorate.service.serviceInterface.UserServiceable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmorateApplicationTests {

    private final UserServiceable userServiceable;
    private final FilmServiceable filmServiceable;

    @Test
    @Order(1)
    public void getAllUsers() {
        List<User> users = userServiceable.get();
        assertTrue(users.isEmpty());
    }

    @Test
    @Order(2)
    public void createUser() {
        User user = User.builder()
                .name("name")
                .login("login")
                .email("test@test.ru")
                .birthday(LocalDate.of(1982, 01, 31))
                .friends(new TreeSet<>())
                .friendship(new HashMap<>())
                .build();
        userServiceable.create(user);
        assertEquals(1, userServiceable.get(1).getId());
    }

    @Test
    @Order(3)
    public void updateUser() {
        User updateUser = User.builder()
                .id(1)
                .name("updated name")
                .login("login")
                .email("test@test.ru")
                .birthday(LocalDate.of(1982, 01, 31))
                .friends(new TreeSet<>())
                .friendship(new HashMap<>())
                .build();
        userServiceable.upDate(updateUser);
        assertEquals(updateUser.getName(), userServiceable.get(1).getName());
    }

    @Test
    @Order(4)
    public void getUserById() {
        User user = userServiceable.get(1);
        assertTrue(user != null);
    }

    @Test
    @Order(5)
    public void getUserBadId() {
        NotFoundException e = Assertions.assertThrows(NotFoundException.class
                , () -> userServiceable.get(-1)
                , "user not found id=-1");
        assertTrue(e.getMessage().equals("user not found id=-1"));
    }

    @Test
    @Order(6)
    public void addFriend() {
        User user = User.builder()
                .name("friend")
                .login("loginFriend")
                .email("testFriend@test.ru")
                .birthday(LocalDate.of(1982, 02, 20))
                .friends(new TreeSet<>())
                .friendship(new HashMap<>())
                .build();
        userServiceable.create(user);
        assertEquals(2, userServiceable.get(2).getId());
        userServiceable.addFriend(1, 2);
        assertTrue(userServiceable.getFriends(1).size() == 1);
    }

    @Test
    @Order(7)
    public void removeFriend() {
        userServiceable.removeFriend(1, 2);
        assertTrue(userServiceable.getFriends(1).size() == 0);
    }

    @Test
    @Order(8)
    public void removeUser() {
        userServiceable.remove(2);
        assertTrue(userServiceable.get(1).getId() == 1);
        assertTrue(userServiceable.get().size() == 1);
    }

    @Test
    @Order(9)
    public void createFilm() {
        Rating mpa = new Rating();
        mpa.setId(1);
        Film film = Film.builder()
                .name("newFilm")
                .description("film desc")
                .duration(100500l)
                .releaseDate(LocalDate.of(1970, 05, 25))
                .mpa(mpa)
                .likesFromUserId(new HashMap<>())
                .genres(new ArrayList<>())
                .build();
        filmServiceable.create(film);
        assertTrue(film.getId() == 1);
    }

    @Test
    @Order(10)
    public void updateFilm() {
        Rating mpa = new Rating();
        mpa.setId(3);
        Film film = Film.builder()
                .id(1)
                .name("updated")
                .description("film desc")
                .duration(100500l)
                .releaseDate(LocalDate.of(1970, 05, 25))
                .mpa(mpa)
                .likesFromUserId(new HashMap<>())
                .genres(new ArrayList<>())
                .build();
        filmServiceable.upDate(film);
        assertTrue(film.getName().equals("updated") &&
                film.getId() == 1 && film.getMpa().getId() == 3);
    }

    @Test
    @Order(11)
    public void getFilmById() {
        Film film = filmServiceable.get(1);
        assertNotNull(film);
    }

    @Test
    @Order(12)
    public void getFilmBadId() {
        NotFoundException e = Assertions.assertThrows(NotFoundException.class,
                () -> filmServiceable.get(-1)
                , "film not found id=-1");
        assertTrue(e.getMessage().equals("film not found id=-1"));
    }

    @Test
    @Order(13)
    public void addFilmLike() {
        filmServiceable.addLike(1, 1);
        assertTrue(filmServiceable.get(1).getLikesFromUserId().containsKey(1));
    }

    @Test
    @Order(14)
    public void removeFilmLike() {
        filmServiceable.removeLike(1, 1);
        assertTrue(filmServiceable.get(1).getLikesFromUserId().isEmpty());
    }

    @Test
    @Order(15)
    public void getPopularFilms() {
        Rating mpa = new Rating();
        mpa.setId(4);
        Film film = Film.builder()
                .name("secondFilm")
                .description("film desc")
                .duration(500l)
                .releaseDate(LocalDate.of(1985, 05, 25))
                .mpa(mpa)
                .likesFromUserId(new HashMap<>())
                .genres(new ArrayList<>())
                .build();
        filmServiceable.create(film);
        //на новый фильм добавим лайк, на новом фильме id должно получиться = 2
        filmServiceable.addLike(film.getId(), 1);
        //на фильме, который первый в базе, удалим лайк
        filmServiceable.removeLike(1, 1);

        List<Film> populars = filmServiceable.getPopularFilms(2);
        assertTrue(populars.get(0).getId() == 2);
        assertTrue(populars.size() == 2);
    }

    @Test
    @Order(15)
    public void removeFilmById() {
        filmServiceable.remove(1);
        assertNotNull(filmServiceable.get(2));
        assertTrue(filmServiceable.get().size() == 1);
    }
}
