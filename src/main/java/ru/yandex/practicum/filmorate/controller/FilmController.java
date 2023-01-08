package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.CustomValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends AbstractController<Film> {
    CustomValidator validator = new CustomValidator<Film>();

    @PostMapping()
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) throws ValidationException {
        validator.validate(film);
        data.put(++id, film);
        film.setId(id);
        log.info("film created '{}'", film);
        return ResponseEntity.ok(film);
    }

    @PutMapping()
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) throws ValidationException {
        validator.validate(film);
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
}
