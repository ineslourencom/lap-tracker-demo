package com.laptracker.service.domain;

import com.laptracker.persistence.PassageRepository;
import com.laptracker.persistence.entity.Kart;
import com.laptracker.persistence.entity.Passage;
import com.laptracker.persistence.entity.Race;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PassageService {

    private final PassageRepository passageRepository;

    public PassageService(PassageRepository passageRepository) {
        this.passageRepository = passageRepository;
    }

    @Transactional
    public void recordPassage(Kart kart, Race race, LocalDateTime timestamp) {
        Passage passage = new Passage();
        passage.setKart(kart);
        passage.setRace(race);
        passage.setPassageTime(timestamp);
        passageRepository.save(passage);
    }
}
