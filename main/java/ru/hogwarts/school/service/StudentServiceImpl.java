package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exeption.FacultyNotFoundException;
import ru.hogwarts.school.exeption.StudentNotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.Collections;

@Service
public class StudentServiceImpl implements StudentService{

private final StudentRepository studentRepository;
@Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }
    @Override
    public Student readStudent(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("Student not found"));
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
    public Student getStudent(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException("Student not found"));
    }
    @Override
    public Student updateStudent(Long id, Student student) {
        Student existing = getStudent(id);
        existing.setName(student.getName());
        existing.setAge(student.getAge());
        studentRepository.save(existing);
        return existing;
    }

}
