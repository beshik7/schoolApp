package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.hogwarts.school.model.Faculty;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FacultyServiceImplTest {
    @Mock
    Map<Long, Faculty> mockMap;

    @InjectMocks
    FacultyServiceImpl facultyService;

    private Faculty faculty;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        faculty = new Faculty(1L, "Engineering", "Red");
    }
    @Test
    void createFacultyTest() {
        when(mockMap.put(any(Long.class), any(Faculty.class))).thenReturn(faculty);
        Faculty created = facultyService.createFaculty(faculty);
        verify(mockMap, times(1)).put(faculty.getId(), faculty);
        assertEquals(faculty, created);
    }
    @Test
    void updateFacultyTest() {
        Faculty updatedFaculty = new Faculty(1L, "Science", "Blue");
        mockMap.put(faculty.getId(), faculty);
        when(mockMap.get(any(Long.class))).thenReturn(updatedFaculty);
        Faculty result = facultyService.updateFaculty(faculty.getId(), updatedFaculty);
        verify(mockMap, times(1)).put(faculty.getId(), updatedFaculty);
        assertEquals(updatedFaculty, result);
    }
    @Test
    void getFilteredByColorTest() {
        Faculty faculty1 = new Faculty(1L, "Engineering", "Red");
        Faculty faculty2 = new Faculty(2L, "Science", "Blue");
        Faculty faculty3 = new Faculty(3L, "Arts", "Blue");

        Map<Long, Faculty> testMap = new HashMap<>();
        testMap.put(faculty1.getId(), faculty1);
        testMap.put(faculty2.getId(), faculty2);
        testMap.put(faculty3.getId(), faculty3);

        when(mockMap.values()).thenReturn(testMap.values());
        assertEquals(2, facultyService.getFilteredByColor("Blue").size());
        verify(mockMap, times(1)).values();
    }
    @Test
    void getFacultyTest() {
        mockMap.put(faculty.getId(), faculty);
        when(mockMap.get(any(Long.class))).thenReturn(faculty);
        Faculty fetched = facultyService.getFaculty(faculty.getId());
        verify(mockMap, times(1)).get(faculty.getId());
        assertEquals(faculty, fetched);
    }

}
