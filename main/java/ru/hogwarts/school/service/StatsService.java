package ru.hogwarts.school.service;

import java.util.List;
import java.util.OptionalDouble;

public interface StatsService {
    List<String> getNamesStartingWithA();
    OptionalDouble getAverageAge();
    String getLongestFacultyName();



}
