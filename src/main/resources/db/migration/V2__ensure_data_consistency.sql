ALTER TABLE karts
    ADD CONSTRAINT uq_karts_id_race UNIQUE (id, race_id);

ALTER TABLE passages
    DROP CONSTRAINT fk_passages_kart;

ALTER TABLE passages
    ADD CONSTRAINT fk_passages_kart_race FOREIGN KEY (kart_id, race_id) REFERENCES karts (id, race_id);
