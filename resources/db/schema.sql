DROP TABLE IF EXISTS team_coach CASCADE;
DROP TABLE IF EXISTS coach CASCADE;
DROP TABLE IF EXISTS tour_match CASCADE;
DROP TABLE IF EXISTS tour CASCADE;
DROP TABLE IF EXISTS table_place CASCADE;
DROP TABLE IF EXISTS season CASCADE;
DROP TABLE IF EXISTS national_championship CASCADE;
DROP TABLE IF EXISTS goal CASCADE;
DROP TABLE IF EXISTS team_player CASCADE;
DROP TABLE IF EXISTS player CASCADE;
DROP TABLE IF EXISTS football_position CASCADE;
DROP TABLE IF EXISTS country CASCADE;
DROP TABLE IF EXISTS match_team CASCADE;
DROP TABLE IF EXISTS team CASCADE;
DROP TABLE IF EXISTS profile_bet CASCADE;
DROP TABLE IF EXISTS match_bet CASCADE;
DROP TABLE IF EXISTS bet CASCADE;
DROP TABLE IF EXISTS matches CASCADE;
DROP TABLE IF EXISTS balance_transaction CASCADE;
DROP TABLE IF EXISTS profile_role CASCADE;
DROP TABLE IF EXISTS role CASCADE;
DROP TABLE IF EXISTS profile CASCADE;

CREATE TABLE profile
(
    id       BIGSERIAL PRIMARY KEY NOT NULL,
    login    VARCHAR(20)           NOT NULL,
    password VARCHAR(100)           NOT NULL,
    firstname VARCHAR(150),
    lastname VARCHAR(150),
    email VARCHAR(150),
    balance DECIMAL(8,2) NOT NULL,
    dob DATE
);

CREATE TABLE role
(
    id       BIGSERIAL PRIMARY KEY NOT NULL ,
    name    VARCHAR(25)           NOT NULL
);

CREATE TABLE profile_role
(
    id       BIGSERIAL PRIMARY KEY NOT NULL ,
    profile_id BIGINT REFERENCES profile(id),
    role_id BIGINT REFERENCES role(id)
);

CREATE TABLE balance_transaction
(
    id       BIGSERIAL PRIMARY KEY NOT NULL ,
    profile_id BIGINT REFERENCES profile(id),
    amount DECIMAL(8,2) NOT NULL,
    is_positive BOOLEAN NOT NULL
);

CREATE TABLE matches
(
    id       BIGSERIAL PRIMARY KEY NOT NULL ,
    name    VARCHAR(155)           NOT NULL,
    date_time TIMESTAMP,
    is_played BOOLEAN NOT NULL
);

CREATE TABLE bet
(
    id       BIGSERIAL PRIMARY KEY NOT NULL ,
    name    VARCHAR(200)           NOT NULL
);

CREATE TABLE match_bet
(
    id       BIGSERIAL PRIMARY KEY NOT NULL ,
    matches_id BIGINT REFERENCES matches(id),
    bet_id BIGINT REFERENCES bet(id),
    coefficient DOUBLE PRECISION NOT NULL
);

CREATE TABLE profile_bet
(
    id       BIGSERIAL PRIMARY KEY NOT NULL ,
    profile_id BIGINT REFERENCES profile(id),
    match_bet_id BIGINT REFERENCES match_bet(id),
    amount DECIMAL(8,2) NOT NULL
);

CREATE TABLE team
(
    id       BIGSERIAL PRIMARY KEY NOT NULL ,
    name    VARCHAR(99)           NOT NULL,
    stadium_name    VARCHAR(99),
    emblem_path     VARCHAR(250)
);

CREATE TABLE match_team
(
    id       BIGSERIAL PRIMARY KEY NOT NULL ,
    matches_id BIGINT REFERENCES matches(id),
    team_id BIGINT REFERENCES team(id)
);

CREATE TABLE country
(
    id       BIGSERIAL PRIMARY KEY NOT NULL ,
    name    VARCHAR(50)           NOT NULL,
    flag_path    VARCHAR(250)
);

CREATE TABLE football_position
(
    id       BIGSERIAL PRIMARY KEY NOT NULL ,
    name    VARCHAR(50)           NOT NULL
);

CREATE TABLE player
(
    id       BIGSERIAL PRIMARY KEY NOT NULL ,
    firstname VARCHAR(150)  NOT NULL,
    lastname VARCHAR(150) NOT NULL,
    dob DATE,
    height INTEGER,
    number INTEGER,
    football_position_id BIGINT REFERENCES football_position(id),
    country_id BIGINT REFERENCES country(id)
);

CREATE TABLE team_player
(
    id       BIGSERIAL PRIMARY KEY NOT NULL ,
    player_id BIGINT REFERENCES player(id),
    team_id BIGINT REFERENCES team(id),
    is_active BOOLEAN NOT NULL
);

CREATE TABLE goal
(
    id       BIGSERIAL PRIMARY KEY NOT NULL ,
    player_id BIGINT REFERENCES player(id),
    match_team_id BIGINT REFERENCES match_team(id),
    goal_minute INTEGER
);

CREATE TABLE national_championship
(
    id       BIGSERIAL PRIMARY KEY NOT NULL ,
    name    VARCHAR(150)           NOT NULL,
    country_id BIGINT REFERENCES country(id),
    emblem_path     VARCHAR(250)
);

CREATE TABLE season
(
    id       BIGSERIAL PRIMARY KEY NOT NULL ,
    national_championship_id BIGINT REFERENCES national_championship(id),
    start_year INTEGER NOT NULL
);

CREATE TABLE table_place
(
    id       BIGSERIAL PRIMARY KEY NOT NULL ,
    season_id BIGINT REFERENCES season(id),
    team_id BIGINT REFERENCES team(id),
    number INTEGER NOT NULL,
    match_count INTEGER NOT NULL,
    points INTEGER NOT NULL,
    difference INTEGER NOT NULL,
    scored INTEGER NOT NULL
);

CREATE TABLE tour
(
    id       BIGSERIAL PRIMARY KEY NOT NULL ,
    number INTEGER NOT NULL,
    season_id BIGINT REFERENCES season(id)
);

CREATE TABLE tour_match
(
    id       BIGSERIAL PRIMARY KEY NOT NULL ,
    tour_id BIGINT REFERENCES tour(id),
    matches_id BIGINT REFERENCES matches(id)
);

CREATE TABLE coach
(
    id       BIGSERIAL PRIMARY KEY NOT NULL ,
    firstname VARCHAR(150)  NOT NULL,
    lastname VARCHAR(150) NOT NULL,
    country_id BIGINT REFERENCES country(id)
);

CREATE TABLE team_coach
(
    id       BIGSERIAL PRIMARY KEY NOT NULL ,
    coach_id BIGINT REFERENCES coach(id),
    team_id BIGINT REFERENCES team(id),
    isActive BOOLEAN NOT NULL
);
