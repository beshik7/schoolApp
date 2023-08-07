package ru.hogwarts.school.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exeption.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(studentService);
    }

    @Test
    void createStudentTest() {
        Student testStudent = new Student("Harry", 17);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);
        Student createdStudent = studentService.createStudent(testStudent);

        assertNotNull(createdStudent);
        assertEquals(testStudent, createdStudent);

        // retrieve the student from service to verify it was added
        when(studentRepository.findById(testStudent.getId())).thenReturn(Optional.of(testStudent));
        Student retrievedStudent = studentService.getStudent(testStudent.getId());
        assertEquals(testStudent, retrievedStudent);
    }

    @Test
    void deleteStudentTest() {
        Student testStudent = new Student("Herrmeion", 17);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);
        studentService.createStudent(testStudent);

        //  to delete the created student
        studentService.deleteStudent(testStudent.getId());
        assertThrows(StudentNotFoundException.class, () -> studentService.getStudent(testStudent.getId()));
    }

    @Test
    void getAllStudentsTest() {
        Student testStudent1 = new Student("Neville", 18);
        Student testStudent2 = new Student("Luna", 17);
        List<Student> students = Arrays.asList(testStudent1, testStudent2);

        when(studentRepository.findAll()).thenReturn(students);
        Collection<Student> retrievedStudents = studentService.getAll();

        assertNotNull(retrievedStudents);
        assertEquals(2, retrievedStudents.size());
        assertTrue(retrievedStudents.contains(testStudent1));
        assertTrue(retrievedStudents.contains(testStudent2));
    }

    @Test
    void filteredByAgeTest() {
        Student testStudent1 = new Student("Ginny", 18);
        Student testStudent2 = new Student("Fred", 18);
        List<Student> students = Arrays.asList(testStudent1, testStudent2);

        when(studentRepository.findAllByAge(18)).thenReturn(students);
        Collection<Student> retrievedStudents = studentService.filteredByAge(18);

        assertNotNull(retrievedStudents);
        assertEquals(2, retrievedStudents.size());
        assertTrue(retrievedStudents.contains(testStudent1));
        assertTrue(retrievedStudents.contains(testStudent2));
    }
    @Test
    public void testGetStudentsByFaculty() {
        // Предположим, что у нас есть факультет с id 1 и два студента, принадлежащих этому факультету.
        Faculty faculty = new Faculty("Faculty1", "Green");
        faculty.setId(1L);

        Student student1 = new Student("John Doe", 20);
        student1.setFaculty(faculty);

        Student student2 = new Student("Jane Doe", 22);
        student2.setFaculty(faculty);

        // Возвращаем этих студентов, когда вызывается метод findByFacultyId в репозитории.
        when(studentRepository.findByFacultyId(1L)).thenReturn(Arrays.asList(student1, student2));

        // Вызываем метод getStudentsByFaculty и проверяем, что он возвращает правильных студентов.
        List<Student> result = studentService.getStudentsByFaculty(1L);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList(student1, student2)));
    }

}