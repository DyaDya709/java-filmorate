package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService implements Serviceable<Film> {
    private final FilmStorage storage;
    private int id = 0;

    private final int MAX_COUNT = 0;

    @Autowired
    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

    private void generateId(final Film film) {
        film.setId(++id);
    }

    @Override
    public void create(Film film) {
        generateId(film);
        storage.put(film.getId(), film);
        log.info("film created '{}'", film);
    }

    @Override
    public void add(Film film) {
        storage.put(film.getId(), film);
    }

    @Override
    public Film get(Integer id) {
        return storage.get(id);
    }

    @Override
    public List<Film> get() {
        log.info("films size '{}'", storage.size());
        return storage.get();
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
        log.info("film updated '{}'",film);
    }

    @Override
    public void remove(Integer id) {
        storage.remove(id);
    }

    public void addLike(Film film, User user) {
        film.getLikesFromUserId().add(user.getId());
    }

    public void addLike(Film film, Integer userId) {
        film.getLikesFromUserId().add(userId);
    }

    public List<Film> getPopularFilms() {
        return storage.get()
                .stream()
                .filter(f -> f.getLikesFromUserId() != null && f.getLikesFromUserId().size() > 0)
                .sorted(Comparator.comparingInt(f -> f.getLikesFromUserId().size()))
                .limit(MAX_COUNT)
                .collect(Collectors.toList());
    }
}
