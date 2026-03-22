package com.laptracker.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.laptracker.persistence.entity.Kart;

@Repository
public interface KartRepository extends JpaRepository<Kart, UUID> {

    Optional<Kart> findByKartNumberAndRaceId(Integer kartNumber, UUID raceId);
}
