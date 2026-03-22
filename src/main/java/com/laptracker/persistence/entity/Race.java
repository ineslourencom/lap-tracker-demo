package com.laptracker.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "race")
public class Race {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "race_seq")
    @SequenceGenerator(name = "race_seq", sequenceName = "lap_sequence", allocationSize = 50)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Enumerated(EnumType.STRING)
    private RaceStatus status;

}
