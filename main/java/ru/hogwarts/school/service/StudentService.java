package ru.hogwarts.school.service;

import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import ru.hogwarts.school.model.Age;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface StudentService {
    Student createStudent(Long facultyId, Student student);
    Student updateStudent(Long Id, Student student);
    Student getStudent(Long id);
    void deleteStudent(Long id);

    Collection<Student> getAll();

    Collection<Student> filteredByAge(int age);

    List<Student> findByAgeBetween(Integer min, Integer max);

    List<Student> getStudentsByFaculty(long facultyId);

    Long countAllStudents();

    Age averageStudentAge();

    List <Student> findLastFiveStudents();
}
