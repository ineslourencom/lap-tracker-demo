package com.laptracker.persistence;

import com.laptracker.persistence.entity.Race;
import com.laptracker.persistence.entity.RaceStatus;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RaceRepository extends JpaRepository<Race, UUID> {

    Optional<Race> findFirstByStatus(RaceStatus status);
}
