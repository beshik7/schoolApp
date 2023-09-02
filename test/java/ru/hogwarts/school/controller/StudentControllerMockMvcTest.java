package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyServiceImpl;
import ru.hogwarts.school.service.StudentServiceImpl;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerMockMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentServiceImpl studentService;

    @MockBean
    private FacultyRepository facultyRepository;
    @MockBean
    private FacultyServiceImpl facultyService;

    @Test
    public void testCreateStudent() throws Exception {
        Faculty mockFaculty = new Faculty();
        Long facultyId = 1L;
        Student student = new Student("John Doe", 20);


        when(facultyRepository.findById(facultyId)).thenReturn(Optional.of(mockFaculty));
        when(studentService.createStudent(any(Long.class), any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.post("/student/faculty/" + facultyId + "/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(student)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.age").value(20));

        verify(studentService, times(1)).createStudent(facultyId, student);
    }

    @Test
    public void testGetAllStudents() throws Exception {
        List<Student> students = List.of(new Student("John Doe", 20));
        when(studentService.getAll()).thenReturn(students);
        mockMvc.perform(MockMvcRequestBuilders.get("/student"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].age").value(20));

        verify(studentService, times(1)).getAll();
    }
    @Test
    public void testUpdateStudent() throws Exception {
        Student existingStudent = new Student("John Doe", 20);
        existingStudent.setId(1L);

        Student updatedStudent = new Student("Jane Doe", 21);
        updatedStudent.setId(1L);

        when(studentService.updateStudent(1L, updatedStudent)).thenReturn(updatedStudent);

        mockMvc.perform(MockMvcRequestBuilders.put("/student/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedStudent)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.age").value(21));

        verify(studentService, times(1)).updateStudent(1L, updatedStudent);
    }

    @Test
    public void testDeleteStudent() throws Exception {
        Long studentId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/student/" + studentId))
                .andExpect(status().isOk());

        verify(studentService, times(1)).deleteStudent(studentId);
    }
    @Test
    public void testFilteredByAge() throws Exception {
        int age = 20;
        List<Student> students = List.of(new Student("John Doe", age));

        when(studentService.filteredByAge(age)).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/age/" + age))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].age").value(age));


        verify(studentService, times(1)).filteredByAge(age);
    }
    @Test
    public void testGetStudentsByAgeRange() throws Exception {
        Integer min = 20;
        Integer max = 25;
        List<Student> students = List.of(
                new Student("John Doe", 21),
                new Student("Jane Smith", 24)
        );

        when(studentService.findByAgeBetween(min, max)).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/age/range")
                        .param("min", min.toString())
                        .param("max", max.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].age").value(21))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"))
                .andExpect(jsonPath("$[1].age").value(24));

        verify(studentService, times(1)).findByAgeBetween(min, max);
    }
    @Test
    public void testGetStudentsByFaculty() throws Exception {
        Long facultyId = 1L;
        List<Student> students = List.of(new Student("John Doe", 21));

        when(studentService.getStudentsByFaculty(facultyId)).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/faculty/" + facultyId))
                .andDo(print())

                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].age").value(21));

        verify(studentService, times(1)).getStudentsByFaculty(facultyId);
    }
    }

