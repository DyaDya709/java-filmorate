package ru.yandex.practicum.filmorate.storage.dbStorage;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.storageInterface.FilmStorage;

import java.util.List;

@Component
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void put(Integer id, Film film) {

    }

    @Override
    public Film get(Integer id) {
        return null;
    }

    @Override
    public List<Film> get() {
        return null;
    }

    @Override
    public void remove(Integer id) {

    }

    @Override
    public void remove(Film film) {

    }

    @Override
    public int size() {
        return 0;
    }
}
