package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
private final StudentRepository studentRepository;
private final FacultyRepository facultyRepository;
@Autowired
    public StudentServiceImpl(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
    this.facultyRepository = facultyRepository;
}

    @Override
    public Student createStudent(Long facultyId, Student student) {
        try {
            Faculty faculty = facultyRepository.findById(facultyId).orElseThrow(() -> new FacultyNotFoundException("Faculty not found"));
            student.setFaculty(faculty);
            return studentRepository.save(student);
        } catch (FacultyNotFoundException e) {
            logger.error("Error while creating student. Faculty with ID: {} not found.", facultyId, e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while creating student.", e);
            throw e;
        }
    }

    @Override
    public void deleteStudent(Long id) {
        logger.info("Method deleteStudent invoked with id: {}", id);
        logger.warn("Attempting to delete student with ID: {}", id);
        studentRepository.deleteById(id);
    }

    @Override
    public Collection<Student> getAll() {
        logger.info("Method getAll invoked");
        logger.debug("Getting all students");
        return Collections.unmodifiableCollection(studentRepository.findAll());
    }

    @Override
    public Collection<Student> filteredByAge(int age) {
        logger.info("Method filteredByAge invoked with age: {}", age);
        return studentRepository.findAllByAge(age);
    }

    @Override
    public List<Student> findByAgeBetween(Integer min, Integer max) {
        logger.info("Method findByAgeBetween invoked with min: {} and max: {}", min, max);
        return studentRepository.findByAgeBetween(min, max);
    }

    @Override
    public Student getStudent(Long id) {
        logger.info("Method getStudent invoked with id: {}", id);
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("Student not found"));
    }
    @Override
    public Student updateStudent(Long id, Student student) {
        logger.info("Method updateStudent invoked with id: {}", id);
        Student existing = getStudent(id);
        existing.setName(student.getName());
        existing.setAge(student.getAge());
        studentRepository.save(existing);
        return existing;
    }

    @Override
    public List<Student> getStudentsByFaculty(long facultyId) {
        logger.info("Method getStudentsByFaculty invoked with facultyId: {}", facultyId);
        return  studentRepository.findByFacultyId(facultyId);
    }
    @Override
    public Long countAllStudents() {
        logger.info("Method countAllStudents invoked");
        return studentRepository.countAllStudents();
    }

    @Override
    public Age averageStudentAge() {
        logger.info("Method averageStudentAge invoked");
        return studentRepository.averageStudentAge();
    }

    @Override
    public List<Student> findLastFiveStudents() {
        logger.info("Method findLastFiveStudents invoked");
        return studentRepository.findLastFiveStudents();
    }


}
