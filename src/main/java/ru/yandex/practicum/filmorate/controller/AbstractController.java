package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;

public abstract class AbstractController<T> {
    protected int id = 0;
    protected HashMap<Integer, T> data = new HashMap<>();

    abstract ResponseEntity<T> create(T element);

    abstract ResponseEntity<T> update(T element);

    abstract ResponseEntity<List<T>> getAllElements();

    abstract ResponseEntity<T> get(Integer id);
}
