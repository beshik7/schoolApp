package ru.hogwarts.school.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exeption.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;
    @Autowired
    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty getFaculty(Long id) {
        return facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException("Faculty not found"));
    }

    @Override
    public Faculty updateFaculty(Long id, Faculty faculty) {
        Faculty existing = getFaculty(id);
        existing.setName(faculty.getName());
        existing.setColor(faculty.getColor());
        return facultyRepository.save(existing);
    }

    @Override
    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    @Override
    public Collection<Faculty> getAll() {
        return facultyRepository.findAll();
    }

    @Override
    public Collection<Faculty> getFilteredByColor(String color) {
        return facultyRepository.getAllByColor(color);
    }

    @Override
    public Collection<Faculty> findByNameOrColorIgnoreCase(String keyword) {
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(keyword, keyword);
    }

}
