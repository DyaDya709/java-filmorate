package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

@Data
@Builder
public class User {
    private int id;
    @Email(message = "invalid email address")
    private String email;
    @NotBlank(message = "user login shouldn't be blank")
    @Pattern(regexp = "(\\S)+")
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
    private Set<Integer> friends = new TreeSet<>();
    private HashMap<Integer, Boolean> friendship = new HashMap<>();
}
