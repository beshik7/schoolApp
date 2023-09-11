package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;
@Service
public class StatsServiceImpl implements  StatsService{

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StatsServiceImpl(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    @Override
    public List<String> getNamesStartingWithA() {
        return studentRepository.findAll()
                .stream()
                .parallel()
                .map(student -> student.getName().toUpperCase())
                .filter(name -> name.startsWith("A"))
                .sorted(String::compareTo)
                .toList();


    }

    @Override
    public OptionalDouble getAverageAge() {
        return studentRepository.findAll()
                .stream()
                .mapToInt(Student::getAge)
                .average();
    }

    @Override
    public String getLongestFacultyName() {
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElse("");
    }
}
