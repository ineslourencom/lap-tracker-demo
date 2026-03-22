package com.laptracker.service.domain;

import com.laptracker.persistence.PassageRepository;
import com.laptracker.persistence.entity.Kart;
import com.laptracker.persistence.entity.Passage;
import com.laptracker.persistence.entity.Race;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PassageService {

    private final PassageRepository passageRepository;



    /**
     * Records a passage for a specific kart and race.
     * @param kart The kart that made the passage.
     * @param race The race in which the passage occurred.
     * @param timestamp The time of the passage.
     */
    @Transactional
    public void recordPassage(Kart kart, Race race, LocalDateTime timestamp) {
        Passage passage = new Passage(race, kart, timestamp);
        passageRepository.save(passage);
    }


    /**
     * Retrieves all passages for a given race.
     * @param race The race for which to retrieve passages.
     * @return A list of all passages for the given race.
     */
    //candidate to search by id
    public List<Passage> getAllByRace(Race race){
        return passageRepository.getPassagesByRace(race);
    }
}
