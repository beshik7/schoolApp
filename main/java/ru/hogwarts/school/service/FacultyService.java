package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface FacultyService {
    Faculty createFaculty(Faculty faculty);
    Faculty getFaculty(Long id);
    Faculty updateFaculty(Long id, Faculty faculty);
    void deleteFaculty(Long id);

    Collection<Faculty> getAll();

    Collection<Faculty> getFilteredByColor(String color);

    Collection<Faculty> findByNameOrColorIgnoreCase(String keyword);
}
