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
    private final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final int MAX_DESCRIPTION_SIZE = 200;

    @PostMapping()
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) throws ValidationException {
        validate(film);
        data.put(++id, film);
        film.setId(id);
        log.info("film created '{}'", film);
        return ResponseEntity.ok(film);
    }

    @PutMapping()
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) throws ValidationException {
        validate(film);
        if (data.containsKey(film.getId())) {
            data.remove(film.getId());
            data.put(film.getId(), film);
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
        if (data.isEmpty()) {
            throw new ValidationException("в библиотеке нет фильмов", 404);
        }
        log.info("films size '{}'", data.size());
        return ResponseEntity.ok(data.values().stream().collect(Collectors.toList()));
    }

    @Override
    public void validate(Film element) throws ValidationException {
        if (element.getName() == null || element.getName().isEmpty()) {
            throw new ValidationException("bad name",400);
        }
        if (element.getDescription() != null && element.getDescription().length() > MAX_DESCRIPTION_SIZE) {
            throw new ValidationException("bad description",400);
        }
        if (element.getReleaseDate() != null && element.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("bad releaseDate",400);
        }
        if (element.getDuration() < 0) {
            throw new ValidationException("bad duration",400);
        }
    }
}
