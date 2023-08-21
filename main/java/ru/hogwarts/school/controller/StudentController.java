package ru.hogwarts.school.controller;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;
import java.util.Collection;
import java.util.List;

    @RestController
    @RequestMapping("/student")
    public class StudentController {
    private final StudentService studentService;
    private final FacultyService facultyService;


    public StudentController(StudentService studentService, FacultyService facultyService) {
        this.studentService = studentService;
        this.facultyService = facultyService;
    }

    @GetMapping
    public Collection<Student> getAll() {
        return studentService.getAll();
    }
    @PostMapping("/faculty/{facultyId}/student")
    public Student createStudent(@PathVariable Long facultyId, @RequestBody Student student) {
        return studentService.createStudent(facultyId, student);
    }

    @PutMapping("{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return studentService.updateStudent(id, student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping("/age/{age}")
    public Collection<Student> filteredByAge(@PathVariable int age) {
        return studentService.filteredByAge(age);
    }

    @GetMapping("/age/range")
    public List<Student> getStudentsByAgeRange(@RequestParam Integer min, @RequestParam Integer max) {
        return studentService.findByAgeBetween(min, max);
    }
    @GetMapping("/faculty/{facultyId}")
    public List<Student> getStudentsByFaculty(@PathVariable Long facultyId) {
        return studentService.getStudentsByFaculty(facultyId);
    }

}
