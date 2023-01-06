package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.HashMap;
import java.util.List;

public abstract class AbstractController<T> {
    protected int id = 0;
    protected HashMap<Integer, T> elements = new HashMap<>();

    abstract ResponseEntity<T> create(T element) throws ValidationException;

    abstract ResponseEntity<T> update(T element) throws ValidationException;

    abstract ResponseEntity<List<T>> getAllElements() throws ValidationException;
}
