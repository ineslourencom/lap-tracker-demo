CREATE SEQUENCE lap_sequence START WITH 1 INCREMENT BY 50;

CREATE TABLE races (
    id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    started_at TIMESTAMP,
    total_laps INT NOT NULL,
    status VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE karts (
    id BIGINT NOT NULL,
    kart_number VARCHAR(255) NOT NULL,
    race_id BIGINT,
    PRIMARY KEY (id)
);

ALTER TABLE karts ADD CONSTRAINT uk_kart_race UNIQUE (kart_number, race_id);

CREATE TABLE laps (
    id BIGINT NOT NULL,
    kart_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    finish_time TIMESTAMP,
    lap_number INTEGER,
    PRIMARY KEY (id)
);

ALTER TABLE karts ADD CONSTRAINT fk_kart_race FOREIGN KEY (race_id) REFERENCES race (id);
ALTER TABLE laps ADD CONSTRAINT fk_lap_kart FOREIGN KEY (kart_id) REFERENCES kart (id);
