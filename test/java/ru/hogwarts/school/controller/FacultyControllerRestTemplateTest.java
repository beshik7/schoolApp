package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import org.assertj.core.api.Assertions;
import ru.hogwarts.school.model.Student;

import java.util.List;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerRestTemplateTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Test
    void testGetAllFaculties() {
        ResponseEntity<List> response = restTemplate.getForEntity( "http://localhost:" + port + "/faculty", List.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    void testCreateFaculty() {
        ResponseEntity<Faculty> response = restTemplate.postForEntity( "http://localhost:" + port + "/faculty", new Faculty("History", "Yellow"), Faculty.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getName()).isEqualTo("History");
    }
    @Test
    void testUpdateFaculty() {
        Faculty faculty = restTemplate.postForEntity("http://localhost:" + port + "/faculty", new Faculty("Math", "Green"), Faculty.class).getBody();

        Faculty updatedFaculty = new Faculty("Mathematics", "Dark Green");
        restTemplate.put("http://localhost:" + port + "/faculty" + "/" + faculty.getId(), updatedFaculty, Faculty.class);

        ResponseEntity<Faculty> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty" + "/" + faculty.getId(), Faculty.class);
        Assertions.assertThat(response.getBody().getName()).isEqualTo("Mathematics");
    }
    @Test
    void testGetFacultyById() {
        ResponseEntity<Faculty> newFacultyResponseEntity =
                restTemplate.postForEntity("http://localhost:" + port + "/faculty", new Faculty("magic", "blue"), Faculty.class);
       Assertions
               .assertThat(newFacultyResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Faculty> facultyResponseEntity =
                restTemplate.getForEntity("http://localhost:" + port + "/faculty/"+newFacultyResponseEntity.getBody().getId(), Faculty.class);
        Assertions
                .assertThat(facultyResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions
                .assertThat(facultyResponseEntity.getBody()).isNotNull();
        Assertions
                .assertThat(facultyResponseEntity.getBody().getName()).isEqualTo("magic");
        Assertions
                .assertThat(facultyResponseEntity.getBody().getColor()).isEqualTo("blue");
    }
    @Test
    void testDeleteFaculty() {
        Faculty faculty = restTemplate.postForEntity("http://localhost:" + port + "/faculty", new Faculty("Arts", "Pink"), Faculty.class).getBody();

        restTemplate.delete("http://localhost:" + port + "/faculty" + "/" + faculty.getId());

        ResponseEntity<Faculty> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty" + "/" + faculty.getId(), Faculty.class);
        Assertions
                .assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    void testFindByNameOrColorIgnoreCase() {
        restTemplate.postForEntity("http://localhost:" + port + "/faculty", new Faculty("Astrology", "Violet"), Faculty.class);

        ResponseEntity<List> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty"+ "/search/Astrology", List.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions
                .assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().size()).isGreaterThanOrEqualTo(1);
    }
    @Test
    void testGetFacultiesByColor() {
        // создаем несколько факультетов разных цветов для этого теста
        restTemplate.postForEntity("http://localhost:" + port + "/faculty", new Faculty("Physics", "Green"), Faculty.class);
        restTemplate.postForEntity("http://localhost:" + port + "/faculty", new Faculty("Chemistry", "Red"), Faculty.class);

        // факультеты по цвету Green
        ResponseEntity<List> facultiesByColorResponse = restTemplate.getForEntity("http://localhost:" + port + "/faculty?color=Green", List.class);
        Assertions
                .assertThat(facultiesByColorResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions
                .assertThat(facultiesByColorResponse.getBody()).isNotNull();
        Assertions
                .assertThat(facultiesByColorResponse.getBody().size()).isGreaterThan(0);
    }
    @Test
    void testGetStudentsByFacultyId() {
        // создаем и сохраняем факультет
        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/faculty",
                new Faculty("Engineering", "Blue"),
                Faculty.class);

        // получаем сохраненный факультет из базы данных
        Faculty faculty = facultyResponseEntity.getBody();

        // создаем студентов и устанавливаем для них этот факультет
        Student student1 = new Student("John Doe", 20);
        Student student2 = new Student("Jane Smith", 21);
        student1.setFaculty(faculty);
        student2.setFaculty(faculty);

        // сохраняем студентов
        restTemplate.postForEntity("http://localhost:" + port + "/students", student1, Student.class);
        restTemplate.postForEntity("http://localhost:" + port + "/students", student2, Student.class);

        // список студентов по facultyId
        ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/" + faculty.getId() + "/students",
                List.class);

        // проверяю что список содержит студентов
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().size()).isGreaterThan(0);
    }

    }

