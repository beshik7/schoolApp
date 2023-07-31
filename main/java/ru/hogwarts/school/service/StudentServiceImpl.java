package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService{
    private Map<Long, Student> studentsMap = new HashMap<>();


    @Override
    public Student createStudent(Student student) {
        studentsMap.put(student.getId(), student);
        return student;
    }
    @Override
    public Student readStudent(Long id) {
        return studentsMap.get(id);
    }
    @Override
    public void deleteStudent(Long id) {
        studentsMap.remove(id);
    }

    @Override
    public Collection<Student> getAll() {
        return Collections.unmodifiableCollection(studentsMap.values());
    }

    @Override
    public Collection<Student> filteredByAge(int age) {
        return studentsMap.values().stream()
                .filter(s -> s.getAge() == age)
                .collect(Collectors.toList());
    }

    @Override
    public Student getStudent(Long id) {
        return studentsMap.get(id);
    }
    @Override
    public Student updateStudent(Long id, Student student) {
        Student existing = getStudent(id);
        existing.setName(student.getName());
        existing.setAge(student.getAge());
        studentsMap.put(id, existing);
        return existing;
    }

}
