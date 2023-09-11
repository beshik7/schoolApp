package ru.hogwarts.school.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exeption.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);
    private final FacultyRepository facultyRepository;
    @Autowired
    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty createFaculty(Faculty faculty) {
        logger.info("Method createFaculty invoked");
        logger.debug("Attempting to create a faculty with name: {}", faculty.getName());
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty getFaculty(Long id) {
        logger.info("Method getFaculty invoked");
        return facultyRepository.findById(id).orElseThrow(() ->  {
            logger.error("Faculty not found with ID: {}", id);
            new FacultyNotFoundException("Faculty not found");
            return new FacultyNotFoundException("Faculty not found");
        });
    }

    @Override
    public Faculty updateFaculty(Long id, Faculty faculty) {
        logger.info("Method updateFaculty invoked for ID: {}", id);
        logger.debug("Attempting to update faculty to name: {}", faculty.getName());
        Faculty existing = getFaculty(id);
        existing.setName(faculty.getName());
        existing.setColor(faculty.getColor());
        return facultyRepository.save(existing);
    }

    @Override
    public void deleteFaculty(Long id) {
        logger.info("Method deleteFaculty invoked");
        logger.warn("Faculty with ID: {} is being deleted", id);
        facultyRepository.deleteById(id);
    }

    @Override
    public Collection<Faculty> getAll() {
        logger.info("Method getAll invoked");
        logger.debug("Getting all faculties");
        return facultyRepository.findAll();
    }

    @Override
    public Collection<Faculty> getFilteredByColor(String color) {
        logger.info("Method getFilteredByColor invoked with color: {}", color);
        return facultyRepository.getAllByColor(color);
    }

    @Override
    public Collection<Faculty> findByNameOrColorIgnoreCase(String keyword) {
        logger.info("Method findByNameOrColorIgnoreCase invoked with keyword: {}", keyword);
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(keyword, keyword);
    }

}
