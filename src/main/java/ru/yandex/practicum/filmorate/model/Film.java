package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@Builder
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
    private List<Genre> genres = new ArrayList<>();
    @NotNull
    private Rating mpa;
}
