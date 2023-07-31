package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {
    private StudentService studentService;

    @GetMapping
    public Collection<Student> getAll() {
        return studentService.getAll();
    }
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping("/{id}")
    public Student readStudent(@PathVariable Long id) {
        return studentService.readStudent(id);
    }

    @PutMapping
    public Student updateStudent(@RequestBody Long id, Student student) {
        return studentService.updateStudent(id, student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping(params = {"age"})
    public Collection<Student> filteredByAge(@PathVariable int age) {
        return studentService.filteredByAge(age);
    }


}
