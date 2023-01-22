package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    void put(Integer id, Film film);

    Film get(Integer id);

    List<Film> get();

    void remove(Integer id);

    void remove(Film film);

    int size();
}
