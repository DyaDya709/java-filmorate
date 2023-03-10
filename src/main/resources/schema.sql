
create table if not exists GENRES
(
    GENRE_ID INTEGER auto_increment
        primary key,
    NAME     CHARACTER VARYING(14) not null
        unique
);

create table if not exists RATINGS
(
    RATING_ID INTEGER auto_increment
        primary key,
    NAME      CHARACTER VARYING(5) not null
        unique
);

create table if not exists FILMS
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

create table if not exists FILMS_GENRES
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

create table if not exists USERS
(
    USER_ID  INTEGER auto_increment
        primary key,
    EMAIL    CHARACTER VARYING(55) not null,
    LOGIN    CHARACTER VARYING(55) not null,
    NAME     CHARACTER VARYING(55),
    BIRTHDAY DATE
);

create table if not exists FRIENDS
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

create table if not exists LIKES
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

DELETE FROM FILMS_GENRES ;
DELETE FROM FRIENDS ;
DELETE FROM LIKES ;
DELETE FROM FILMS ;
ALTER TABLE FILMS ALTER COLUMN film_id RESTART WITH 1;
DELETE FROM RATINGS ;
ALTER TABLE RATINGS ALTER COLUMN rating_id RESTART WITH 1;
DELETE FROM GENRES ;
ALTER TABLE GENRES ALTER COLUMN genre_id RESTART WITH 1;
DELETE FROM USERS ;
ALTER TABLE USERS ALTER COLUMN USER_ID RESTART WITH 1;