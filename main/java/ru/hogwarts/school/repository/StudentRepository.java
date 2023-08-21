package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;

import java.util.List;
@Repository

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByAge(int age);

    List<Student> findByAgeBetween(Integer age1, Integer age2);

    List<Student> findByFacultyId(Long faculty_id);
}
