package com.laptracker.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.laptracker.persistence.entity.Kart;

@Repository
public interface KartRepository extends JpaRepository<Kart, Long> {
    Optional<Kart> findByKartNumber(String kartNumber);

    Optional<Kart> findByKartNumberAndRaceId(String kartNumber, Long raceId);

    java.util.List<Kart> findByRaceId(Long raceId);
}
