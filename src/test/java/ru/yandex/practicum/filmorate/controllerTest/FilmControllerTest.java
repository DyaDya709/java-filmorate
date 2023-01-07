package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    void init() {
        filmController = new FilmController();
    }

    @Test
    @DisplayName("Создать фильм с пустым названием")
    void createFilmEmptyName() {
        Film film = new Film(1, "", "desc",
                LocalDate.of(1982, 01, 01), 100);
        ValidationException ex = Assertions.assertThrows(ValidationException.class,
                () -> filmController.validate(film));
        Assertions.assertEquals("bad name", ex.getMessage());

        film.setName(null);
        ex = Assertions.assertThrows(ValidationException.class,
                () -> filmController.validate(film));
        Assertions.assertEquals("bad name", ex.getMessage());
    }

    @Test
    @DisplayName("Создать фильм с описанием больше макс длины")
    void createFilmDescriptionLength201() {
        String desc = "qwqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqwe" +
                "qweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqwe" +
                "qweqweqweqweqweqweqwqw";
        Film film = new Film(1, "name", "",
                LocalDate.of(1982, 01, 01), 100);
        film.setDescription(desc);
        ValidationException ex = Assertions.assertThrows(ValidationException.class,
                () -> filmController.validate(film));
        Assertions.assertEquals("bad description", ex.getMessage());

    }

    @Test
    @DisplayName("Создать фильм с датой релиза, раньше даты д.р. кино")
    void createFilmBadReleaseDate() {
        Film film = new Film(1, "name", "desc",
                LocalDate.of(1895, 12, 27), 100);
        ValidationException ex = Assertions.assertThrows(ValidationException.class,
                () -> filmController.validate(film));
        Assertions.assertEquals("bad releaseDate", ex.getMessage());

    }

    @Test
    @DisplayName("Создать фильм с продолжительностью меньше нуля")
    void createFilmNegativeDuration() {
        Film film = new Film(1, "name", "desc",
                LocalDate.of(1999, 12, 27), -1);
        ValidationException ex = Assertions.assertThrows(ValidationException.class,
                () -> filmController.validate(film));
        Assertions.assertEquals("bad duration", ex.getMessage());

    }
}
