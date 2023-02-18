package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.serviceInterface.FilmServiceable;
import ru.yandex.practicum.filmorate.service.serviceInterface.UserServiceable;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmorateApplicationTests {

	private final UserServiceable userServiceable;
	private final FilmServiceable filmServiceable;

	@Test
	@Order(1)
	public void getAllUsers() {
		List<User> users = userServiceable.get();
		Assertions.assertTrue(users.isEmpty());
	}
	@Test
	@Order(2)
	public void createUser() {
		User user = new User();
		user.setName("name");
		user.setLogin("login");
		user.setEmail("test@test.ru");
		user.setBirthday(LocalDate.of(1982,01,31));
		userServiceable.create(user);
		Assertions.assertEquals(1,userServiceable.get(1).getId());
	}

	@Test
	@Order(3)
	public void updateUser() {
		User updateUser = new User();
		updateUser.setId(1);
		updateUser.setName("updated name");
		updateUser.setLogin("login");
		updateUser.setEmail("test@test.ru");
		updateUser.setBirthday(LocalDate.of(1982,01,31));
		userServiceable.upDate(updateUser);
		Assertions.assertEquals(updateUser.getName(),userServiceable.get(1).getName());
	}

	@Test
	@Order(4)
	public void getUserById() {
		User user = userServiceable.get(1);
		Assertions.assertTrue(user != null);
	}

	@Test
	@Order(5)
	public void getUserBadId() {
		NotFoundException e = Assertions.assertThrows(NotFoundException.class
				,()->userServiceable.get(-1)
				,"user not found id=-1");
		Assertions.assertTrue(e.getMessage().equals("user not found id=-1"));
	}

	@Test
	@Order(6)
	public void addFriend() {
		User user = new User();
		user.setName("friend");
		user.setLogin("loginFriend");
		user.setEmail("testFriend@test.ru");
		user.setBirthday(LocalDate.of(1982,02,20));
		userServiceable.create(user);
		Assertions.assertEquals(2,userServiceable.get(2).getId());
		userServiceable.addFriend(1,2);
		Assertions.assertTrue(userServiceable.getFriends(1).size() == 1);
	}

	@Test
	@Order(7)
	public void removeFriend() {
		userServiceable.removeFriend(1,2);
		Assertions.assertTrue(userServiceable.getFriends(1).size() == 0);
	}

	@Test
	@Order(8)
	public void removeUser() {
		userServiceable.remove(2);
		Assertions.assertTrue(userServiceable.get(1).getId() == 1);
		Assertions.assertTrue(userServiceable.get().size() == 1);
	}

	@Test
	@Order(9)
	public void createFilm() {
		Film film = new Film();
		film.setName("newFilm");
		film.setDescription("film desc");
		film.setDuration(100500l);
		film.setReleaseDate(LocalDate.of(1970,05,25));
		Rating mpa = new Rating();
		mpa.setId(1);
		film.setMpa(mpa);
		filmServiceable.create(film);
		Assertions.assertTrue(film.getId() == 1);
	}

	@Test
	@Order(10)
	public void updateFilm() {
		Film film = new Film();
		film.setId(1);
		film.setName("updated");
		film.setDescription("film desc");
		film.setDuration(100500l);
		film.setReleaseDate(LocalDate.of(1970,05,25));
		Rating mpa = new Rating();
		mpa.setId(3);
		film.setMpa(mpa);
		filmServiceable.upDate(film);
		Assertions.assertTrue(film.getName().equals("updated") &&
				film.getId() == 1 && film.getMpa().getId() == 3);
	}

	@Test
	@Order(11)
	public void getFilmById() {
		Film film = filmServiceable.get(1);
		Assertions.assertNotNull(film);
	}

	@Test
	@Order(12)
	public void getFilmBadId() {
		NotFoundException e = Assertions.assertThrows(NotFoundException.class,
				()->filmServiceable.get(-1)
				,"film not found id=-1");
		Assertions.assertTrue(e.getMessage().equals("film not found id=-1"));
	}

	@Test
	@Order(13)
	public void addFilmLike() {
		filmServiceable.addLike(1,1);
		Assertions.assertTrue(filmServiceable.get(1).getLikesFromUserId().containsKey(1));
	}

	@Test
	@Order(14)
	public void removeFilmLike() {
		filmServiceable.removeLike(1,1);
		Assertions.assertTrue(filmServiceable.get(1).getLikesFromUserId().isEmpty());
	}

	@Test
	@Order(15)
	public void getPopularFilms() {
		Film film = new Film();
		film.setName("secondFilm");
		film.setDescription("film desc");
		film.setDuration(500l);
		film.setReleaseDate(LocalDate.of(1985,05,25));
		Rating mpa = new Rating();
		mpa.setId(4);
		film.setMpa(mpa);
		filmServiceable.create(film);
		//на новый фильм добавим лайк, на новом фильме id должно получиться = 2
		filmServiceable.addLike(film.getId(), 1);
		//на фильме, который первый в базе, удалим лайк
		filmServiceable.removeLike(1,1);

		List<Film> populars = filmServiceable.getPopularFilms(2);
		Assertions.assertTrue(populars.get(0).getId() == 2);
		Assertions.assertTrue(populars.size() == 2);
	}

	@Test
	@Order(15)
	public void removeFilmById() {
		filmServiceable.remove(1);
		Assertions.assertNotNull(filmServiceable.get(2));
		Assertions.assertTrue(filmServiceable.get().size() == 1);
	}
}
