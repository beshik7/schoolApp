package ru.hogwarts.school.service;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService {
    private Map<Long, Faculty> facultyMap = new HashMap<>();

    @Override
    public Faculty createFaculty(Faculty faculty) {
        facultyMap.put(faculty.getId(), faculty);
        return faculty;
    }

    @Override
    public Faculty getFaculty(Long id) {
        return facultyMap.get(id);
    }

    @Override
    public Faculty updateFaculty(Long id, Faculty faculty) {
        Faculty existing = getFaculty(id);
        existing.setName(faculty.getName());
        existing.setColor(faculty.getColor());
        facultyMap.put(id, existing);
        return existing;
    }

    @Override
    public void deleteFaculty(Long id) {
        facultyMap.remove(id);
    }

    @Override
    public Collection<Faculty> getAll() {
        return Collections.unmodifiableCollection(facultyMap.values());
    }

    @Override
    public Collection<Faculty> getFilteredByColor(String color) {
        return getAll().stream().filter(f -> f.getColor().equalsIgnoreCase(color)).collect(Collectors.toList());
    }

}
