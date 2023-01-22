package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;

public interface Serviceable<T> {
    void create(T element);
    void add(T element);
    T get(Integer id);
    List<T> get();
    void upDate(T element) throws NotFoundException;
    void remove(Integer id);
}
