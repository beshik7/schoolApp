package ru.hogwarts.school.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {
    private StudentServiceImpl studentService;

    @BeforeEach
    public void setup() {
        studentService = new StudentServiceImpl();
    }

    @Test
    void createStudentTest() {
        Student testStudent = new Student(1L, "Harry", 17);
        Student createdStudent = studentService.createStudent(testStudent);

        assertNotNull(createdStudent);
        assertEquals(testStudent, createdStudent);

        // retrieve the student from service to verify it was added
        Student retrievedStudent = studentService.readStudent(testStudent.getId());
        assertEquals(testStudent, retrievedStudent);
    }
    @Test
    void deleteStudentTest() {
        Student testStudent = new Student(1L, "Herrmeion", 17);
        studentService.createStudent(testStudent);

        // try to delete the created student
        studentService.deleteStudent(testStudent.getId());
        assertThrows(NoSuchElementException.class, () -> studentService.readStudent(testStudent.getId()));
    }
    @Test
    void getAllStudentsTest() {
        Student testStudent1 = new Student(2L, "Neville", 18);
        Student testStudent2 = new Student(3L, "Luna", 17);
        studentService.createStudent(testStudent1);
        studentService.createStudent(testStudent2);

        Collection<Student> students = studentService.getAll();

        assertNotNull(students);
        assertEquals(2, students.size());
        assertTrue(students.contains(testStudent1));
        assertTrue(students.contains(testStudent2));
    }
    @Test
    void filteredByAgeTest() {
        Student testStudent1 = new Student(2L, "Ginny", 18);
        Student testStudent2 = new Student(2L, "Fred", 18);
        studentService.createStudent(testStudent1);
        studentService.createStudent(testStudent2);

        Collection<Student> students = studentService.filteredByAge(18);

        assertNotNull(students);
        assertEquals(2, students.size());
        assertTrue(students.contains(testStudent1));
    }


}