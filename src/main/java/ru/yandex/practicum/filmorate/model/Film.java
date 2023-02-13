package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.enumCustom.Genre;
import ru.yandex.practicum.filmorate.enumCustom.Rating;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Film {
    private int id;
    @NotBlank(message = "название не может быть пустым")
    private String name;
    private String description;
    @PastOrPresent
    private LocalDate releaseDate;
    @Positive
    private long duration;
    private Integer rate;
    private HashMap<Integer, Integer> likesFromUserId = new HashMap<>();
    private Set<Genre> genres = new HashSet<>();
    private Rating rating;
    public Film(int id, String name, String description, LocalDate releaseDate, long duration, Integer rate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
    }
    public Film(String name, String description, LocalDate releaseDate, long duration, Integer rate) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
    }
}
