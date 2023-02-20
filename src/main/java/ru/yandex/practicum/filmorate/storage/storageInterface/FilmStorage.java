package ru.yandex.practicum.filmorate.storage.storageInterface;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

public interface FilmStorage {

    void put(Film film);

    Film update(Film film);

    Film get(Integer id);

    List<Film> get();

    void remove(Integer id);

    void remove(Film film);

    List<Film> getPopularFilms(Integer count);

    void addLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);

    Rating getRating(Integer id);

    List<Rating> getRating();

    Genre getGenre(Integer id);

    List<Genre> getGenre();

}
