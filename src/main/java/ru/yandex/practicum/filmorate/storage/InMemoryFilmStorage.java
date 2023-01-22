package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final HashMap<Integer, Film> DATA = new HashMap<>();

    @Override
    public void put(Integer id, Film film) {
        DATA.put(id, film);
    }

    @Override
    public Film get(Integer id) {
        return DATA.get(id);
    }

    @Override
    public List<Film> get() {
        return DATA.values().stream().collect(Collectors.toList());
    }

    @Override
    public void remove(Integer id) {
        DATA.remove(id);
    }

    @Override
    public void remove(Film film) {
        DATA.remove(film.getId());
    }

    @Override
    public int size() {
        return DATA.size();
    }
}
