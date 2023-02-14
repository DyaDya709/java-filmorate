package ru.yandex.practicum.filmorate.storage.dbStorage;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.storageInterface.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
        film.setDuration(rs.getLong("duration"));
        Rating mpa = new Rating();
        mpa.setId(rs.getInt("rating_id"));
        mpa.setName(rs.getString("mpaname"));
        film.setMpa(mpa);
        film.setGenres(getFilmGenres(film.getId()));
        return film;
    }

    private Rating makeRating(ResultSet rs) throws SQLException {
        Rating mpa = new Rating();
        mpa.setId(rs.getInt("rating_id"));
        mpa.setName(rs.getString("name"));
        return mpa;
    }
    private Genre makeGenre(ResultSet rs) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("genre_id"));
        return genre;
    }

    private Set<Genre> getFilmGenres(Integer filmId) {
        String sql = "SELECT GENRE_ID FROM FILMS_GENRES WHERE FILM_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), filmId).stream().collect(Collectors.toSet());
    }

    private Map<String, Object> filmToMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("RELEASE_DATE", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rating_id", film.getMpa().getId());
        return values;
    }

    private void updateFilmsGenres(Film film) {
        String sqlDel = "DELETE FROM FILMS_GENRES WHERE FILM_ID=?";
        jdbcTemplate.update(sqlDel, film.getId());
        String sqlInsertGenre = "INSERT INTO FILMS_GENRES(FILM_ID,GENRE_ID) VALUES(?,?)";
        film.getGenres().forEach((genre -> jdbcTemplate.update(sqlInsertGenre, film.getId(), genre.getId())));
    }

    @Override
    public void put(Integer id, Film film) {
        //заглушка для совместимости с инрефейсом
    }

    @Override
    public void put(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("film_id");
        int film_id = simpleJdbcInsert.executeAndReturnKey(filmToMap(film)).intValue();
        //Засетим id, так как этот объект будет возвращен клиенту, не будем собирать его из базы заново.
        film.setId(film_id);
        updateFilmsGenres(film);
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?,RATING_ID =? " +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getName(),film.getDescription(),film.getReleaseDate(),film.getDuration()
                ,film.getMpa().getId(), film.getId());
        updateFilmsGenres(film);
        return film;
    }

    @Override
    public Film get(Integer id) {
        String sql = "SELECT f.FILM_ID,\n" +
                "       f.NAME AS filmName,\n" +
                "       f.DESCRIPTION,\n" +
                "       f.RELEASE_DATE,\n" +
                "       f.DURATION,\n" +
                "       f.RATING_ID,\n" +
                "       r.NAME AS mpaname\n" +
                " FROM FILMS AS f \n" +
                "    LEFT JOIN RATINGS AS r ON f.RATING_ID = r.RATING_ID\n" +
                " WHERE f.FILM_ID = ?";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeFilm(rs)), id).stream().findFirst().orElse(null);
    }

    @Override
    public List<Film> get() {
        String sql = "SELECT f.FILM_ID,\n" +
                "       f.NAME AS filmName,\n" +
                "       f.DESCRIPTION,\n" +
                "       f.RELEASE_DATE,\n" +
                "       f.DURATION,\n" +
                "       f.RATING_ID,\n" +
                "       r.NAME AS mpaname\n" +
                " FROM FILMS AS f \n" +
                "    LEFT JOIN RATINGS AS r ON f.RATING_ID = r.RATING_ID";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeFilm(rs)));
    }

    @Override
    public void remove(Integer id) {
        String sql = "DELETE FROM FILMS WHERE FILM_ID=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void remove(Film film) {
        String sql = "DELETE FROM FILMS WHERE FILM_ID=?";
        jdbcTemplate.update(sql, film.getId());
    }

    @Override
    public int size() {
        return 0;
    }
}
