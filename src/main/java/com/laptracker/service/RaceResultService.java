package com.laptracker.service;

import com.laptracker.api.model.response.LapDetailResponse;
import com.laptracker.api.model.response.RaceResultResponse;
import com.laptracker.api.model.response.TotalRaceResult;
import com.laptracker.persistence.entity.Passage;
import com.laptracker.service.domain.PassageService;
import com.laptracker.service.domain.RaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RaceResultService {

    private final PassageService passageService;
    private final RaceService raceService;


    private static final String DURATION_FORMAT = "%02d:%02d.%03d";

    /**
     * Retrieves and calculates the race results.
     *
     * @param raceId The ID of the race.
     * @return A {@link RaceResultResponse} containing the winner and fastest lap.
     */
    @Transactional(readOnly = true)
    public RaceResultResponse getRaceResult(UUID raceId) {
        log.info("Calculating results from race id {} results", raceId);
        
        var race = raceService.findById(raceId);
        var passages = passageService.getAllByRace(race);
        var lapResultsByKart = calculateLapResults(passages, race.getStartedAt());

        var totalRaceResults = calculateTotalRaceResults(lapResultsByKart);

        var winnerKart = calculateWinner(totalRaceResults);
        var fastestLap = findFastestLap(lapResultsByKart);

        return RaceResultResponse.builder()
                .winnerKart(winnerKart)
                .fastestLap(fastestLap.orElse(null))
                .build();
    }

    /**
     * Groups a list of passages by kart number.
     *
     * @param passages The list of passages to group.
     * @return A map where the key is the kart number and the value is a list of passages for that kart.
     */
    private Map<Integer, List<Passage>> groupPassagesByKart(List<Passage> passages) {
        return passages.stream()
                .collect(Collectors.groupingBy(p -> p.getKart().getKartNumber()));
    }

    /**
     * Generates a list of lap details for a given list of kart passages.
     *
     * @param kartPassages The list of passages for a single kart, sorted by passage time.
     * @param raceStartedAt The time the race started.
     * @return A list of {@link LapDetailResponse} for the kart.
     */
    private List<LapDetailResponse> generateLapsForKart(List<Passage> kartPassages, LocalDateTime raceStartedAt) {

        List<LapDetailResponse> laps = new ArrayList<>();
        LocalDateTime previousPassageTime = raceStartedAt;

        // no use for streams since we need to retrieve by lap number
        for (int i = 0; i < kartPassages.size(); i++) {
            Passage currentPassage = kartPassages.get(i);
            Duration lapDuration = Duration.between(previousPassageTime, currentPassage.getPassageTime());

            laps.add(LapDetailResponse.builder()
                            .kartNumber(currentPassage.getKart().getKartNumber())
                            .lapNumber(i + 1)
                            .startTime(previousPassageTime)
                            .finishTime(currentPassage.getPassageTime())
                            .durationValue(lapDuration)
                            .duration(formatDuration(lapDuration))
                    .build());

            previousPassageTime = currentPassage.getPassageTime();
        }

        return laps;
    }

    /**
     * Calculates the lap results for all karts in the race.
     *
     * @param passages The list of all passages in the race.
     * @param raceStartedAt The time the race started.
     * @return A map where the key is the kart number and the value is a list of lap details.
     */
    private Map<Integer, List<LapDetailResponse>> calculateLapResults(List<Passage> passages, LocalDateTime raceStartedAt) {
        var passagesByKart = groupPassagesByKart(passages);

        return passagesByKart.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            var kartPassages = entry.getValue();
                            kartPassages.sort(Comparator.comparing(Passage::getPassageTime));
                            return generateLapsForKart(kartPassages, raceStartedAt);
                        }
                ));
    }

    private Map<Integer, TotalRaceResult> calculateTotalRaceResults(Map<Integer, List<LapDetailResponse>> lapResultsByKart) {
        return lapResultsByKart.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            var laps = entry.getValue();
                             var totalDuration = laps.stream()
                                     .map(LapDetailResponse::getDurationValue)
                                     .reduce(Duration.ZERO, Duration::plus); //sum all timmings per kart
                            return new TotalRaceResult(totalDuration, laps.size());
                        }
                ));
    }

    /**
     * Calculates the winner of the race.
     * The winner is the kart with the most laps, then the fastest total time.
     *
     * @param totalRaceResults A map of total race results by kart number.
     * @return The kart number of the winner.
     */
    private int calculateWinner(Map<Integer, TotalRaceResult> totalRaceResults) {
        return totalRaceResults.entrySet().stream()
                // Filter out karts that haven't completed any laps
                .filter(entry -> entry.getValue().lapCount() > 0)
                // Sort by laps (DESC) then total duration (ASC)
                .sorted((e1, e2) -> {
                    // Sort by Lap Count DESC
                    int lapCompare = Integer.compare(e2.getValue().lapCount(), e1.getValue().lapCount());
                    if (lapCompare != 0) {
                        return lapCompare;
                    }
                    // Sort by Total Duration ASC
                    return e1.getValue().totalDuration().compareTo(e2.getValue().totalDuration());
                })
                .map(Map.Entry::getKey) // extract the kart number
                .findFirst()
                .orElse(0);
    }

    /**
     * Finds the fastest lap among all karts in the race.
     *
     * @param lapResultsByKart A map of lap results by kart number.
     * @return An {@link Optional} containing the fastest {@link LapDetailResponse}, or empty if no laps were completed.
     */
     private Optional<LapDetailResponse> findFastestLap(Map<Integer, List<LapDetailResponse>> lapResultsByKart) {
        return lapResultsByKart.values().stream()
                .flatMap(List::stream)
                .min(Comparator.comparing(LapDetailResponse::getDurationValue));
    }

    /**
     * Formats a {@link Duration} into a "mm:ss.SSS" string.
     *
     * @param duration The duration to format.
     * @return A formatted string representing the duration.
     */
    private String formatDuration(Duration duration) {
        long minutes = duration.toMinutes();
        long seconds = duration.toSecondsPart();
        long millis = duration.toMillisPart();
        return String.format(DURATION_FORMAT, minutes, seconds, millis);
    }
}
