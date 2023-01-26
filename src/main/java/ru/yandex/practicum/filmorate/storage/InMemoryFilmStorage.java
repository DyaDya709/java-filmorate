package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Integer, Film> data = new HashMap<>();

    @Override
    public void put(Integer id, Film film) {
        data.put(id, film);
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
    public int size() {
        return data.size();
    }
}
