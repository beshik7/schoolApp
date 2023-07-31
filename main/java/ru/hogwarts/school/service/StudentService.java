package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface StudentService {
    Student createStudent(Student student);
    Student readStudent(Long id);
    Student updateStudent(Long id, Student student);
    Student getStudent(Long id);
    void deleteStudent(Long id);

    Collection<Student> getAll();

    Collection<Student> filteredByAge(int age);
}
