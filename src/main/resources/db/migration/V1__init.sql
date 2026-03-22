CREATE TABLE races
(
    id         UUID         NOT NULL,
    name       VARCHAR(255) NOT NULL,
    total_laps INT          NOT NULL,
    status     VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE karts
(
    id          UUID   NOT NULL,
    kart_number INT NOT NULL,
    race_id     UUID,
    PRIMARY KEY (id)
);

CREATE TABLE passages
(
    id           UUID      NOT NULL,
    race_id      UUID      NOT NULL,
    kart_id      UUID      NOT NULL,
    passage_time TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE karts
    ADD CONSTRAINT fk_kart_race FOREIGN KEY (race_id) REFERENCES races (id);

ALTER TABLE passages
    ADD CONSTRAINT fk_passages_race FOREIGN KEY (race_id) REFERENCES races (id);
