package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    @Email(message = "invalid email address")
    private String email;
    @NotBlank(message = "user login shouldn't be blank")
    @Pattern(regexp ="(\\S)+")
    private String login;

    private String name;
    @PastOrPresent
    private LocalDate birthday;
}
