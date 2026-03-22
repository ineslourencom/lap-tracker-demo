package com.laptracker.persistence.entity;

import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "races")
@Data
@NoArgsConstructor
@Getter
@Setter
public class Race {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String raceName;

    @Column(name = "total_laps", nullable = false)
    private Integer totalLaps;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RaceStatus status;

    @OneToMany(mappedBy = "race", cascade = CascadeType.ALL)
    private List<Kart> karts = new ArrayList<>();

    public Race(String raceName, Integer totalLaps, RaceStatus status, LocalDateTime startedAt) {
        this.raceName = raceName;
        this.totalLaps = totalLaps;
        this.status = status;
        this.startedAt = startedAt;
    }
}
