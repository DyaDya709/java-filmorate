package ru.yandex.practicum.filmorate.storage.memoryStorage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.storageInterface.FilmStorage;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage  {
    private final HashMap<Integer, Film> data = new HashMap<>();

    @Override
    public void put(Integer id, Film film) {
        data.put(id, film);
    }

    @Override
    public void put(Film film) {

    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public Film get(Integer id) {
        return data.get(id);
    }

    @Override
    public List<Film> get() {
        return data.values().stream().collect(Collectors.toList());
    }

    @Override
    public void remove(Integer id) {
        data.remove(id);
    }

    @Override
    public void remove(Film film) {
        data.remove(film.getId());
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return null;
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {

    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {

    }

    @Override
    public Rating getRating(Integer id) {
        return null;
    }

    @Override
    public List<Rating> getRating() {
        return null;
    }

    @Override
    public Genre getGenre(Integer id) {
        return null;
    }

    @Override
    public List<Genre> getGenre() {
        return null;
    }

    @Override
    public int size() {
        return data.size();
    }
}
