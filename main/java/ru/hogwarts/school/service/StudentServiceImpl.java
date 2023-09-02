package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exeption.FacultyNotFoundException;
import ru.hogwarts.school.exeption.StudentNotFoundException;
import ru.hogwarts.school.model.Age;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService{

private final StudentRepository studentRepository;
private final FacultyRepository facultyRepository;
@Autowired
    public StudentServiceImpl(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
    this.facultyRepository = facultyRepository;
}

    @Override
    public Student createStudent(Long facultyId, Student student) {
        Faculty faculty = facultyRepository.findById(facultyId).orElseThrow(() -> new FacultyNotFoundException("Faculty not found"));
        student.setFaculty(faculty);
        student = studentRepository.save(student);
        return student;
    }

    @Override
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public Collection<Student> getAll() {
        return Collections.unmodifiableCollection(studentRepository.findAll());
    }

    @Override
    public Collection<Student> filteredByAge(int age) {
        return studentRepository.findAllByAge(age);
    }

    @Override
    public List<Student> findByAgeBetween(Integer min, Integer max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    @Override
    public Student getStudent(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("Student not found"));
    }
    @Override
    public Student updateStudent(Long id, Student student) {
        Student existing = getStudent(id);
        existing.setName(student.getName());
        existing.setAge(student.getAge());
        studentRepository.save(existing);
        return existing;
    }

    @Override
    public List<Student> getStudentsByFaculty(long facultyId) {
    return  studentRepository.findByFacultyId(facultyId);
    }
    @Override
    public Long countAllStudents() {
        return studentRepository.countAllStudents();
    }

    @Override
    public Age averageStudentAge() {
        return studentRepository.averageStudentAge();
    }

    @Override
    public List<Student> findLastFiveStudents() {
        return studentRepository.findLastFiveStudents();
    }


}
