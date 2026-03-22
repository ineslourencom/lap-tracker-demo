CREATE TABLE races
(
    id         BIGINT       NOT NULL,
    name       VARCHAR(255) NOT NULL,
    started_at TIMESTAMP,
    total_laps INT          NOT NULL,
    status     VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE laps
(
    id           BIGINT NOT NULL,
    kart_id      BIGINT NOT NULL,
    passage_time TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE karts
(
    id          BIGINT       NOT NULL,
    kart_number BIGINT NOT NULL,
    race_id     BIGINT,
    PRIMARY KEY (id)
);

ALTER TABLE karts
    ADD CONSTRAINT fk_kart_race FOREIGN KEY (race_id) REFERENCES races (id);

ALTER TABLE laps
    ADD CONSTRAINT fk_lap_kart FOREIGN KEY (kart_id) REFERENCES karts (id);
