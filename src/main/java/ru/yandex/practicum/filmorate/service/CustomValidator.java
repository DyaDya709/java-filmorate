package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Service
public class CustomValidator<T> {
    private final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final int MAX_DESCRIPTION_SIZE = 200;

    public void validate(T element) throws ValidationException {
        if (element instanceof User) {
            User user = (User) element;
            if (user.getEmail() == null || !user.getEmail().contains("@") || user.getEmail().isEmpty()) {
                throw new ValidationException("bad email", 400);
            }
            if (user.getLogin() == null || user.getLogin().contains(" ") || user.getLogin().isEmpty()) {
                throw new ValidationException("bad login", 400);
            }
            if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("bad birthday", 400);
            }
        } else if (element instanceof Film) {
            Film film = (Film) element;
            if (film.getName() == null || film.getName().isEmpty()) {
                throw new ValidationException("bad name", 400);
            }
            if (film.getDescription() != null && film.getDescription().length() > MAX_DESCRIPTION_SIZE) {
                throw new ValidationException("bad description", 400);
            }
            if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
                throw new ValidationException("bad releaseDate", 400);
            }
            if (film.getDuration() < 0) {
                throw new ValidationException("bad duration", 400);
            }
        }
    }
}
