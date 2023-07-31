package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private FacultyService facultyService;

    @GetMapping
    public Collection<Faculty> getAll() {
        return facultyService.getAll();
    }

    @GetMapping(params = {"color"})
    public Collection<Faculty> getFilteredByColor(@RequestParam String color) {
        return facultyService.getFilteredByColor(color);
    }


    @GetMapping("/{id}")
    public Faculty getFacultyId(@PathVariable Long id) {
        return facultyService.getFaculty(id);
    }
    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
       return facultyService.createFaculty(faculty);
    }

    @DeleteMapping("/{id}")
    public void deleteFaculty(@PathVariable Long id) {
         facultyService.deleteFaculty(id);
    }
    @PutMapping("/{id}")
    public Faculty updateFaculty(@PathVariable Long id, @RequestBody Faculty faculty) {
        return facultyService.updateFaculty(id, faculty);

    }

}
