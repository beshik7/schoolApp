package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FacultyServiceImplTest {
    @Mock
    private FacultyRepository facultyRepository; // замена мокированного Map

    @InjectMocks
    private FacultyServiceImpl facultyService;

    private Faculty faculty;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        faculty = new Faculty("Engineering", "Red");
    }

    @Test
    void createFacultyTest() {
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        Faculty created = facultyService.createFaculty(faculty);
        verify(facultyRepository, times(1)).save(faculty);
        assertEquals(faculty, created);
    }

    @Test
    void updateFacultyTest() {
        Faculty updatedFaculty = new Faculty("Science", "Blue");
        when(facultyRepository.findById(faculty.getId())).thenReturn(Optional.of(faculty));
        when(facultyRepository.save(updatedFaculty)).thenReturn(updatedFaculty);
        Faculty result = facultyService.updateFaculty(faculty.getId(), updatedFaculty);
        assertEquals(updatedFaculty, result);
    }

    @Test
    void getFilteredByColorTest() {
        Faculty faculty1 = new Faculty("Engineering", "Red");
        Faculty faculty2 = new Faculty("Science", "Blue");
        Faculty faculty3 = new Faculty("Arts", "Blue");

        List<Faculty> faculties = Arrays.asList(faculty2, faculty3);

        when(facultyRepository.getAllByColor("Blue")).thenReturn(faculties);
        assertEquals(2, facultyService.getFilteredByColor("Blue").size());
    }

    @Test
    void getFacultyTest() {
        when(facultyRepository.findById(faculty.getId())).thenReturn(Optional.of(faculty));
        Faculty fetched = facultyService.getFaculty(faculty.getId());
        verify(facultyRepository, times(1)).findById(faculty.getId());
        assertEquals(faculty, fetched);
    }
}