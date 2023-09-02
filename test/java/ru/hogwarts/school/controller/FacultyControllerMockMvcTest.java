package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyServiceImpl;
import ru.hogwarts.school.service.StudentServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
public class FacultyControllerMockMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @SpyBean
    private FacultyServiceImpl facultyService;

    @MockBean
    private FacultyRepository facultyRepository;
    @MockBean
    private StudentServiceImpl studentService;
    @Test
    public void testGetFacultyById() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Engineers");
        faculty.setColor("Black");
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(new Faculty(1L, "Engineers", "Black")));
        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/1")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Engineers"))
                .andExpect(jsonPath("$.color").value("Black"));
        verify(facultyRepository, times(1)).findById(1L);
        verify(facultyService, times(1)).getFaculty(1L);

}
    @Test
    public void testGetAllFaculties() throws Exception {
        List<Faculty> faculties = Collections.singletonList(new Faculty(1L, "Engineers", "Black"));
        when(facultyRepository.findAll()).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Engineers"))
                .andExpect(jsonPath("$[0].color").value("Black"));
    }
    @Test
    public void testGetFacultyByColor() throws Exception {
        List<Faculty> faculties = Collections.singletonList(new Faculty(1L, "Engineers", "Black"));
        when(facultyRepository.getAllByColor("Black")).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty").param("color", "Black"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Engineers"))
                .andExpect(jsonPath("$[0].color").value("Black"));
    }
    @Test
    public void testCreateFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "Engineers", "Black");
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(faculty)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Engineers"))
                .andExpect(jsonPath("$.color").value("Black"));
    }
    @Test
    public void testUpdateFaculty() throws Exception {
        Faculty existingFaculty = new Faculty(1L, "Engineers", "Black");
        Faculty updatedFaculty = new Faculty(1L, "Scientists", "White");

        when(facultyRepository.findById(1L)).thenReturn(Optional.of(existingFaculty));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(updatedFaculty);

        mockMvc.perform(MockMvcRequestBuilders.put("/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedFaculty)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Scientists"))
                .andExpect(jsonPath("$.color").value("White"));
    }
    @Test
    public void testDeleteFaculty() throws Exception {
        doNothing().when(facultyRepository).deleteById(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/faculty/1"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(facultyRepository, times(1)).deleteById(1L);
    }
    @Test
    public void testSearchFacultyByNameOrColor() throws Exception {
        List<Faculty> faculties = Collections.singletonList(new Faculty(1L, "Engineers", "Black"));
        when(facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase("Engineers", "Engineers")).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/search/Engineers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Engineers"))
                .andExpect(jsonPath("$[0].color").value("Black"));
    }
    @Test
    public void testGetStudentsByFacultyId() throws Exception {
        List<Student> students = Collections.singletonList(new Student("John Doe", 20));
        when(studentService.getStudentsByFaculty(1L)).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/1/students"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].age").value(20));
    }

}