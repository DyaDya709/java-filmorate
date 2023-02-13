package ru.yandex.practicum.filmorate.service.sericeInterface;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmServiceable {
    void create(Film element);

    void add(Film element);

    Film get(Integer id) throws NotFoundException;

    List<Film> get();

    void upDate(Film element) throws NotFoundException;

    void remove(Integer id);

    boolean addLike(Integer filmId, Integer userId) throws NotFoundException;

    boolean removeLike(Integer filmId, Integer userId);

    List<Film> getPopularFilms(Integer count);
}
