package ru.yandex.practicum.filmorate.storage.dbStorage;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Rating mpa = new Rating();
        mpa.setId(rs.getInt("rating_id"));
        mpa.setName(rs.getString("mpaname"));

        Film film = Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                .duration(rs.getLong("duration"))
                .genres(new ArrayList<>())
                .likesFromUserId(new HashMap<>())
                .mpa(mpa).build();
        film.setGenres(getFilmGenres(film.getId()));
        film.setLikesFromUserId(getFilmLikes(film.getId()));
        //используется для определения популярного фильма
        if (rs.getMetaData().getColumnCount() == 8 && rs.getMetaData().getColumnName(8).equals("LIKES")) {
            film.setRate(rs.getInt("likes"));
        }
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
        genre.setName(rs.getString("name"));
        return genre;
    }

    private List<Genre> getFilmGenres(Integer filmId) {
        String sql = "SELECT fg.GENRE_ID," +
                "       g.NAME" +
                " FROM FILMS_GENRES AS fg" +
                "    LEFT JOIN GENRES AS g ON fg.GENRE_ID = g.GENRE_ID" +
                " WHERE FILM_ID = ?" +
                " ORDER BY fg.GENRE_ID";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), filmId).stream().collect(Collectors.toList());
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
        if (film.getGenres() == null) {
            film.setGenres(new ArrayList<>());
        }
        film.getGenres().stream()
                .distinct()
                .forEach((genre -> jdbcTemplate.update(sqlInsertGenre, film.getId(), genre.getId())));
        film.setGenres(getFilmGenres(film.getId()));
    }

    private HashMap<Integer, Integer> getFilmLikes(Integer filmId) {
        HashMap<Integer, Integer> likes = new HashMap<>();
        String sql = "SELECT USER_ID," +
                "COUNT(FILM_ID) AS count " +
                "FROM LIKES where FILM_ID=?" +
                "group by USER_ID";
        SqlRowSet likesSet = jdbcTemplate.queryForRowSet(sql,filmId);
        while (likesSet.next()) {
            likes.put(likesSet.getInt("user_id"),likesSet.getInt("count"));
        }
        return likes;
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
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration()
                , film.getMpa().getId(), film.getId());
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
    public List<Film> getPopularFilms(Integer count) {
        String sql = "SELECT f.FILM_ID,\n" +
                "       f.NAME,\n" +
                "       f.DESCRIPTION,\n" +
                "       f.RELEASE_DATE,\n" +
                "       f.DURATION,\n" +
                "       f.RATING_ID,\n" +
                "       r.NAME AS mpaname,\n" +
                "       COUNT(l.FILM_ID) AS likes\n" +
                "FROM FILMS AS f\n" +
                "         LEFT JOIN LIKES l on f.FILM_ID = l.FILM_ID\n" +
                "         LEFT JOIN RATINGS AS r ON f.RATING_ID = r.RATING_ID\n" +
                "group by f.FILM_ID,f.NAME,f.DESCRIPTION,f.RELEASE_DATE,f.DURATION,f.RATING_ID,r.NAME\n" +
                "ORDER BY  likes DESC\n" +
                "LIMIT ?";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeFilm(rs)), count);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        String sql = "INSERT INTO LIKES(user_id, film_id) VALUES (?,?)";
        jdbcTemplate.update(sql, userId, filmId);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        String sql = "DELETE FROM LIKES where USER_ID=? AND FILM_ID=?";
        jdbcTemplate.update(sql, userId, filmId);
    }

    @Override
    public Rating getRating(Integer id) {
        String sql = "SELECT * FROM RATINGS where RATING_ID=?";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeRating(rs)), id).stream().findFirst().orElse(null);
    }

    @Override
    public List<Rating> getRating() {
        String sql = "SELECT * FROM RATINGS where RATING_ID ORDER BY RATING_ID";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeRating(rs)));
    }

    @Override
    public Genre getGenre(Integer id) {
        String sql = "SELECT * FROM GENRES where GENRE_ID=?";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeGenre(rs)), id).stream().findFirst().orElse(null);
    }

    @Override
    public List<Genre> getGenre() {
        String sql = "SELECT * FROM GENRES ORDER BY GENRE_ID";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeGenre(rs)));
    }

}
