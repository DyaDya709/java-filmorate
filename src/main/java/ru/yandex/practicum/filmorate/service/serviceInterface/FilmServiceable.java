package ru.yandex.practicum.filmorate.service.serviceInterface;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

public interface FilmServiceable {
    void create(Film element);

    Film get(Integer id) throws NotFoundException;

    List<Film> get();

    void upDate(Film element) throws NotFoundException;

    void remove(Integer id);

    boolean addLike(Integer filmId, Integer userId) throws NotFoundException;

    boolean removeLike(Integer filmId, Integer userId);

    List<Film> getPopularFilms(Integer count);

    Rating getRating(Integer id) throws NotFoundException;

    List<Rating> getRating();

    Genre getGenre(Integer id);

    List<Genre> getGenre();
}
