package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.HashMap;
import java.util.List;

public abstract class AbstractController<T> {
    protected int id = 0;
    protected HashMap<Integer, T> data = new HashMap<>();

    abstract ResponseEntity<T> create(T element) throws ValidationException;

    abstract ResponseEntity<T> update(T element) throws ValidationException, NotFoundException;

    abstract ResponseEntity<List<T>> getAllElements() throws ValidationException;

    abstract ResponseEntity<T> get(Integer id) throws NotFoundException;
}
