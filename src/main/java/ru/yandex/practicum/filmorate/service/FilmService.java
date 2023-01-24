package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService implements Serviceable<Film> {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private int id = 0;

    private final int MAX_COUNT = 0;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    private void generateId(final Film film) {
        film.setId(++id);
    }

    @Override
    public void create(Film film) {
        generateId(film);
        filmStorage.put(film.getId(), film);
        log.info("film created '{}'", film);
    }

    @Override
    public void add(Film film) {
        filmStorage.put(film.getId(), film);
    }

    @Override
    public Film get(Integer id) {
        return filmStorage.get(id);
    }

    @Override
    public List<Film> get() {
        log.info("films size '{}'", filmStorage.size());
        return filmStorage.get();
    }

    @Override
    public void upDate(Film film) throws NotFoundException {
        Film oldFilm = get(film.getId());
        if (oldFilm == null) {
            String text = String.format("film id %d not found", film.getId());
            throw new NotFoundException(text);
        }
        remove(oldFilm.getId());
        add(film);
        log.info("film updated '{}'", film);
    }

    @Override
    public void remove(Integer id) {
        filmStorage.remove(id);
    }

    public boolean addLike(Integer filmId, Integer userId) {
        if (userStorage.get(userId) != null) {
            get(filmId).getLikesFromUserId().add(userId);
            return true;
        }
        return false;
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.get()
                .stream()
                .filter(f -> f.getLikesFromUserId() != null && f.getLikesFromUserId().size() > 0)
                .sorted(Comparator.comparingInt(f -> f.getLikesFromUserId().size()))
                .limit(count == null ? MAX_COUNT : count)
                .collect(Collectors.toList());
    }
}
