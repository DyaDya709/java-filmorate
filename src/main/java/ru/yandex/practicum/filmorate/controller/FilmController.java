package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends AbstractController<Film> {
    private final LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);
    private final int maxDescriptionSize = 200;

    @PostMapping()
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getDescription() != null && film.getDescription().length() > maxDescriptionSize) {
            throw new ValidationException("max length description is 200 characters", 400);
        }
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(minReleaseDate)) {
            throw new ValidationException("release date — no earlier than December 28, 1895", 400);
        }
        elements.put(++id, film);
        film.setId(id);
        log.info("film created '{}'", film);
        return ResponseEntity.ok(film);
    }

    @PutMapping()
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getDescription() != null && film.getDescription().length() > maxDescriptionSize) {
            throw new ValidationException("max length description is 200 characters", 400);
        }
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(minReleaseDate)) {
            throw new ValidationException("release date — no earlier than December 28, 1895", 400);
        }
        if (elements.containsKey(film.getId())) {
            elements.remove(film.getId());
            elements.put(film.getId(), film);
            log.info("film updated '{}'", film);
            return ResponseEntity.ok(film);
        } else {
            throw new ValidationException("unknown film id", 404);
        }
    }

    //в этом случае ValidationException будет обрабатываться в @RestControllerAdvice
    //ApplicationExceptionsHandler
    @GetMapping()
    ResponseEntity<List<Film>> getAllElements() throws ValidationException {
        if (elements.isEmpty()) {
            throw new ValidationException("в библиотеке нет фильмов", 404);
        }
        log.info("films size '{}'", elements.size());
        return ResponseEntity.ok(elements.values().stream().collect(Collectors.toList()));
    }
}
