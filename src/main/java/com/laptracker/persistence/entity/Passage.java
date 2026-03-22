package com.laptracker.persistence.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "passages")
public class Passage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Race race;

    @ManyToOne
    private Kart kart;

    private LocalDateTime passageTime;

    public Passage(Race race, Kart kart, LocalDateTime passageTime) {
        this.race = race;
        this.kart = kart;
        this.passageTime = passageTime;
    }
}
