package ru.yandex.practicum.filmorate.service.memoryService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.sericeInterface.FilmServiceable;
import ru.yandex.practicum.filmorate.storage.storageInterface.FilmStorage;
import ru.yandex.practicum.filmorate.storage.storageInterface.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService implements FilmServiceable {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private int id = 0;
    private final int DEFAULT_COUNT = 10;

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
        Film film = filmStorage.get(id);
        if (film == null) {
            throw new NotFoundException("film not found id=" + id);
        }
        return film;
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

    public boolean addLike(Integer filmId, Integer userId) throws NotFoundException {
        if (userStorage.get(userId) != null) {
            Integer likes = get(filmId).getLikesFromUserId().getOrDefault(userId, 0);
            get(filmId).getLikesFromUserId().put(userId
                    , likes + 1);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeLike(Integer filmId, Integer userId) {
        if (userStorage.get(userId) != null) {
            get(filmId).getLikesFromUserId().remove(userId);
            return true;
        } else {
            throw new NotFoundException("user not found id=" + userId);
        }

    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        Comparator<Film> rateComparator = Comparator
                .comparing(f -> filmLikes(f.getId()) + f.getRate(), Comparator.reverseOrder());
        Comparator<Film> dateComparator = Comparator
                .comparing((f) -> f.getReleaseDate(), Comparator.nullsLast(Comparator.reverseOrder()));

        List<Film> films = filmStorage.get()
                .stream()
                .filter(f -> f.getLikesFromUserId().size() > 0 || f.getRate() > 0)
                .sorted(dateComparator.thenComparing(rateComparator))
                .limit(count == null ? DEFAULT_COUNT : count)
                .collect(Collectors.toList());
        return films;
    }

    @Override
    public Rating getRating(Integer id) throws NotFoundException {
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

    private Integer filmLikes(Integer filmId) {
        return filmStorage.get(filmId).getLikesFromUserId().values()
                .stream()
                .mapToInt(l -> l).sum();
    }
}
