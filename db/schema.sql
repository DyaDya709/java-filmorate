drop table FILMS_GENRES;
drop table FRIENDS;
drop table GENRES;
drop table LIKES;
drop table FILMS;
drop table RATINGS;
drop table USERS;

create table GENRES
(
    GENRE_ID INTEGER auto_increment
        primary key,
    NAME     CHARACTER VARYING(14) not null
        unique
);

create table RATINGS
(
    RATING_ID INTEGER auto_increment
        primary key,
    NAME      CHARACTER VARYING(5) not null
        unique
);

create table FILMS
(
    FILM_ID       INTEGER auto_increment
        primary key,
    NAME          CHARACTER VARYING(50),
    DESCRIPTION   CHARACTER VARYING(200),
    RELEASE_DATE DATE,
    DURATION      BIGINT,
    RATING_ID     INTEGER,
    constraint FILM_RATING_RATING_ID_FK
        foreign key (RATING_ID) references RATINGS
            on update cascade on delete cascade
);

create table FILMS_GENRES
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint FILMS_GENRES_PK
        primary key (FILM_ID, GENRE_ID),
    constraint GENRES_FILM_FILM_ID_FK
        foreign key (FILM_ID) references FILMS
            on update cascade on delete cascade,
    constraint GENRES_GENRE_GENRE_ID_FK
        foreign key (GENRE_ID) references GENRES
            on update cascade on delete cascade
);

create table USERS
(
    USER_ID  INTEGER auto_increment
        primary key,
    EMAIL    CHARACTER VARYING(55) not null,
    LOGIN    CHARACTER VARYING(55) not null,
    NAME     CHARACTER VARYING(55),
    BIRTHDAY DATE
);

create table FRIENDS
(
    USER_ID   INTEGER               not null,
    FRIEND_ID INTEGER               not null,
    CONFIRMED BOOLEAN default FALSE not null,
    constraint FRIENDS_PK
        primary key (USER_ID, FRIEND_ID),
    constraint FRIENDS_USER_TABLE_USER_ID_FK
        foreign key (USER_ID) references USERS
            on update cascade on delete cascade,
    constraint FRIENDS_USER_TABLE_USER_ID_FK_2
        foreign key (FRIEND_ID) references USERS
            on update cascade on delete cascade
);

create table LIKES
(
    USER_ID INTEGER not null,
    FILM_ID INTEGER not null,
    constraint LIKES_PK
        primary key (USER_ID, FILM_ID),
    constraint LIKES_FILM_FILM_ID_FK
        foreign key (FILM_ID) references FILMS
            on update cascade on delete cascade,
    constraint LIKES_USER_TABLE_USER_ID_FK
        foreign key (USER_ID) references USERS
            on update cascade on delete cascade
);

INSERT INTO GENRES(NAME) VALUES ('Комедия');
INSERT INTO GENRES(NAME) VALUES ('Драма');
INSERT INTO GENRES(NAME) VALUES ('Мультфильм');
INSERT INTO GENRES(NAME) VALUES ('Триллер');
INSERT INTO GENRES(NAME) VALUES ('Документальный');
INSERT INTO GENRES(NAME) VALUES ('Боевик');

INSERT INTO RATINGS(NAME) VALUES ('G');
INSERT INTO RATINGS(NAME) VALUES ('PG');
INSERT INTO RATINGS(NAME) VALUES ('PG-13');
INSERT INTO RATINGS(NAME) VALUES ('R');
INSERT INTO RATINGS(NAME) VALUES ('NC-17');