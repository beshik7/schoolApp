package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Age;
import ru.hogwarts.school.model.Student;

import java.awt.print.Pageable;
import java.util.List;
@Repository

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByAge(int age);

    List<Student> findByAgeBetween(Integer age1, Integer age2);

    List<Student> findByFacultyId(Long faculty_id);
    @Query(value = "select count(*) from student s", nativeQuery = true)
    Long countAllStudents();
    @Query(value = "SELECT AVG(s.age) FROM Student s", nativeQuery = true)
    Age averageStudentAge();
    @Query(value ="SELECT * FROM Student ORDER BY id DESC limit 5", nativeQuery = true)
    List<Student> findLastFiveStudents();
}
