package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.service.StatsService;

import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Stream;

@RestController
@RequestMapping("/stats")
public class StatsController {
    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/names-starts-with-a")
    public List<String> getNamesStartingWithA() {
        return statsService.getNamesStartingWithA();
    }

    @GetMapping("/average-age")
    public OptionalDouble getAverageAge() {
        return statsService.getAverageAge();
    }

    @GetMapping("/longest-faculty-name")
    public String getLongestFacultyName() {
        return statsService.getLongestFacultyName();

    }
    @GetMapping("/calc-sum")
    public Integer calculateSum() {
        return Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .parallel()  // параллельный стрим для ускорения
                .reduce(0, Integer::sum);
    }
}
