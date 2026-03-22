package com.laptracker.util;

import com.laptracker.api.model.request.PassageRecordRequest;
import com.laptracker.api.model.request.StartRaceRequest;
import com.laptracker.persistence.entity.Kart;
import com.laptracker.persistence.entity.Passage;
import com.laptracker.persistence.entity.Race;
import com.laptracker.persistence.entity.RaceStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class TestFixtures {

    public static StartRaceRequest createRaceRequest(int karts, int totalLaps) {
        List<Integer> kartList = IntStream.rangeClosed(1, karts)
                .boxed()
                .toList();
        return new StartRaceRequest("KartLeMans", totalLaps, kartList);
    }

    public static PassageRecordRequest createRecordPassageRequest(Integer kartNumber, LocalDateTime timestamp) {
        return new PassageRecordRequest(kartNumber, timestamp);
    }

    public static Race newRace(LocalDateTime raceStart){
        return new Race("KartLeMans", 5, RaceStatus.STARTED,raceStart);
    }

    public static Kart newKart(int kartNumber, Race race){
        return new Kart(kartNumber, race);
    }

    public static Passage newPassage(Race race, Kart kart, LocalDateTime time){
        return new Passage(UUID.randomUUID(), race, kart, time);
    }

}
