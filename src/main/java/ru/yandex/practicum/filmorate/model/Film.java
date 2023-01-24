package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
@AllArgsConstructor
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
    private HashMap<Integer,Integer> likesFromUserId = new HashMap<>();
}
