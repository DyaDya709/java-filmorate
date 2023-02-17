package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.service.serviceInterface.FilmServiceable;
import ru.yandex.practicum.filmorate.service.serviceInterface.UserServiceable;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

	private final UserServiceable userServiceable;
	private final FilmServiceable filmServiceable;

	@Test
	public void getAllUsers() {
		userServiceable.get();
	}
}
