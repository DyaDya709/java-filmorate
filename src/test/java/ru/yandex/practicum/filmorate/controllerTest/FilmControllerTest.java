package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.CustomValidator;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

public class FilmControllerTest {
    FilmController filmController;
    CustomValidator validator;
    FilmService filmService;
    InMemoryFilmStorage storage;
    InMemoryUserStorage userStorage;

    @BeforeEach
    void init() {
        storage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        validator = new CustomValidator<Film>();
        filmService = new FilmService(storage, userStorage);
        filmController = new FilmController(validator, filmService);
    }

    @Test
    @DisplayName("Создать фильм с пустым названием")
    void createFilmEmptyName() {
        Film film = new Film(1, "", "desc",
                LocalDate.of(1982, 01, 01), 100,0);
        ValidationException ex = Assertions.assertThrows(ValidationException.class,
                () -> validator.validate(film));
        Assertions.assertEquals("bad name", ex.getMessage());

        film.setName(null);
        ex = Assertions.assertThrows(ValidationException.class,
                () -> validator.validate(film));
        Assertions.assertEquals("bad name", ex.getMessage());
    }

    @Test
    @DisplayName("Создать фильм с описанием больше макс длины")
    void createFilmDescriptionLength201() {
        String desc = "qwqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqwe" +
                "qweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqwe" +
                "qweqweqweqweqweqweqwqw";
        Film film = new Film(1, "name", "",
                LocalDate.of(1982, 01, 01), 100, 0);
        film.setDescription(desc);
        ValidationException ex = Assertions.assertThrows(ValidationException.class,
                () -> validator.validate(film));
        Assertions.assertEquals("bad description", ex.getMessage());

    }

    @Test
    @DisplayName("Создать фильм с датой релиза, раньше даты д.р. кино")
    void createFilmBadReleaseDate() {
        Film film = new Film(1, "name", "desc",
                LocalDate.of(1895, 12, 27), 100, 0);
        ValidationException ex = Assertions.assertThrows(ValidationException.class,
                () -> validator.validate(film));
        Assertions.assertEquals("bad releaseDate", ex.getMessage());

    }

    @Test
    @DisplayName("Создать фильм с продолжительностью меньше нуля")
    void createFilmNegativeDuration() {
        Film film = new Film(1, "name", "desc",
                LocalDate.of(1999, 12, 27), -1, 0);
        ValidationException ex = Assertions.assertThrows(ValidationException.class,
                () -> validator.validate(film));
        Assertions.assertEquals("bad duration", ex.getMessage());

    }
}
