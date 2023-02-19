package ru.yandex.practicum.filmorate.service.dbService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.serviceInterface.FilmServiceable;
import ru.yandex.practicum.filmorate.service.serviceInterface.UserServiceable;
import ru.yandex.practicum.filmorate.storage.storageInterface.FilmStorage;

import java.util.List;

@Service
@Primary
@Slf4j
public class FilmDbService implements FilmServiceable {
    private final FilmStorage filmStorage;
    private final UserServiceable userServiceable;
    private final int DEFAULT_COUNT = 10;

    @Autowired
    public FilmDbService(FilmStorage filmStorage, UserServiceable userServiceable) {
        this.filmStorage = filmStorage;
        this.userServiceable = userServiceable;
    }

    @Override
    public void create(Film film) {
        filmStorage.put(film);
        log.info("film created '{}'", film);
    }

    @Override
    public Film get(Integer id) {
        Film film = filmStorage.get(id);
        if (film == null) {
            throw new NotFoundException("film not found id=" + id);
        }
        return film;
    }

    @Override
    public List<Film> get() {
        log.info("films size '{}'", "get all films");
        return filmStorage.get();
    }

    @Override
    public void upDate(Film film) throws NotFoundException {
        Film oldFilm = get(film.getId());
        if (oldFilm == null) {
            String text = String.format("film id %d not found", film.getId());
            throw new NotFoundException(text);
        }
        filmStorage.update(film);
        log.info("film updated '{}'", film);
    }

    @Override
    public void remove(Integer id) {
        filmStorage.remove(id);
    }

    public boolean addLike(Integer filmId, Integer userId) throws NotFoundException {
        //проверим есть ли пользователь в базе
        userServiceable.get(userId);
        if (filmStorage.get(filmId) != null) {
            filmStorage.addLike(filmId, userId);
            return true;
        } else {
            throw new NotFoundException("film not found id=" + filmId);
        }
    }

    @Override
    public boolean removeLike(Integer filmId, Integer userId) {
        //проверим есть ли пользователь в базе
        userServiceable.get(userId);
        if (filmStorage.get(filmId) != null) {
            filmStorage.removeLike(filmId, userId);
            return true;
        } else {
            throw new NotFoundException("film not found id=" + filmId);
        }
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getPopularFilms(count == null ? DEFAULT_COUNT : count);
    }

    @Override
    public Rating getRating(Integer id) throws NotFoundException {
        Rating rating = filmStorage.getRating(id);
        if (rating == null) {
            throw new NotFoundException("mpa not found id=" + id);
        }
        return rating;
    }

    @Override
    public List<Rating> getRating() {
        return filmStorage.getRating();
    }

    @Override
    public Genre getGenre(Integer id) {
        Genre genre = filmStorage.getGenre(id);
        if (genre == null) {
            throw new NotFoundException("genre not found id=" + id);
        }
        return genre;
    }

    @Override
    public List<Genre> getGenre() {
        return filmStorage.getGenre();
    }
}
