package com.laptracker.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(name = "kart", uniqueConstraints = {
    @UniqueConstraint(name = "uk_kart_race", columnNames = {"kart_number", "race_id"})
})
public class Kart {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "kart_seq")
    @SequenceGenerator(name = "kart_seq", sequenceName = "lap_sequence", allocationSize = 50)
    private Long id;

    @Column(name = "kart_number", nullable = false)
    private String kartNumber;

    @Column(name = "race_id")
    private Long raceId;

}
