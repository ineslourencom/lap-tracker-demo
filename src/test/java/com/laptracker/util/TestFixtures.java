package com.laptracker.util;

import com.laptracker.api.model.request.StartRaceRequest;
import com.laptracker.persistence.entity.Kart;

import java.util.stream.LongStream;

class TestFixtures {

    public static StartRaceRequest createRaceRequest(int karts, int totalLaps){
        var kartList = LongStream.rangeClosed(1, karts)
                .boxed()
                .toList();
        return new StartRaceRequest("KartLeMans", totalLaps, kartList);

    }

    public static Kart createKart(){
        new Kart()
                .setId();
    }


}
