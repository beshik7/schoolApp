package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerRestTemplateTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetAllStudents() {
        // Создаем факультет
        Faculty faculty = new Faculty("Engineering", "Blue");
        ResponseEntity<Faculty> facultyResponse = restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        Long facultyId = facultyResponse.getBody().getId();

        // Создаем студента
        Student initialStudent = new Student("John", 20);
        restTemplate.postForEntity("http://localhost:" + port + "/student/faculty/" + facultyId + "/student", initialStudent, Student.class);

        ResponseEntity<List> response = restTemplate.getForEntity("http://localhost:" + port + "/student", List.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().size()).isGreaterThan(0);
    }
    @Test
    void testCreateStudent() {
        Faculty faculty = new Faculty("Engineering", "Blue");
        ResponseEntity<Faculty> facultyResponse = restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        Assertions.assertThat(facultyResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(facultyResponse.getBody()).isNotNull();
        Long facultyId = facultyResponse.getBody().getId();

        Student student = new Student("John", 20);
        ResponseEntity<Student> studentResponse = restTemplate.postForEntity("http://localhost:" + port + "/student/faculty/" + facultyId + "/student", student, Student.class);

        Assertions.assertThat(studentResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(studentResponse.getBody()).isNotNull();
        Assertions.assertThat(studentResponse.getBody().getName()).isEqualTo("John");
    }

    @Test
    void testUpdateStudent() {
        // Создаем факультет
        Faculty faculty = new Faculty("Engineering", "Blue");
        ResponseEntity<Faculty> facultyResponse = restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        Long facultyId = facultyResponse.getBody().getId();

        // Создаем студента
        Student initialStudent = new Student("John", 20);
        ResponseEntity<Student> createdStudentResponse = restTemplate.postForEntity("http://localhost:" + port + "/student/faculty/" + facultyId + "/student", initialStudent, Student.class);
        Long studentId = createdStudentResponse.getBody().getId();

        // Обновляем студента
        Student updatedStudent = new Student("John Updated", 21);
        restTemplate.put("http://localhost:" + port + "/student/" + studentId, updatedStudent);

        ResponseEntity<Student> updatedResponse = restTemplate.getForEntity("http://localhost:" + port + "/student/" + studentId, Student.class);
        Assertions.assertThat(updatedResponse.getBody().getName()).isEqualTo("John Updated");
        Assertions.assertThat(updatedResponse.getBody().getAge()).isEqualTo(21);
    }

    @Test
    void testDeleteStudent() {
        // Создаем факультет
        Faculty faculty = new Faculty("Engineering", "Blue");
        ResponseEntity<Faculty> facultyResponse = restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        Long facultyId = facultyResponse.getBody().getId();

        // Создаем студента
        Student initialStudent = new Student("John", 20);
        ResponseEntity<Student> createdStudentResponse = restTemplate.postForEntity("http://localhost:" + port + "/student/faculty/" + facultyId + "/student", initialStudent, Student.class);
        Long studentId = createdStudentResponse.getBody().getId();

        // Удаляем студента
        restTemplate.delete("http://localhost:" + port + "/student/" + studentId);
        ResponseEntity<Student> response = restTemplate.getForEntity("http://localhost:" + port + "/student/" + studentId, Student.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    void testGetStudentsByAge() {
        // Сначала создаем студента с возрастом 20
        ResponseEntity<Student> newStudentResponseEntity =
                restTemplate.postForEntity("http://localhost:" + port + "/student/faculty/1/student", new Student("John", 20), Student.class);
        Assertions.assertThat(newStudentResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Теперь проверяем, можем ли мы получить студентов с возрастом 20
        ResponseEntity<List<Student>> response = restTemplate.exchange("http://localhost:" + port + "/student/age/20", HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {});
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().get(0).getName()).isEqualTo("John");
        Assertions.assertThat(response.getBody().get(0).getAge()).isEqualTo(20);
    }

    @Test
    void testGetStudentsByAgeRange() {
        // Сначала создаем студентов с разными возрастами
        restTemplate.postForEntity("http://localhost:" + port + "/student/faculty/1/student", new Student("John", 19), Student.class);
        restTemplate.postForEntity("http://localhost:" + port + "/student/faculty/1/student", new Student("Jane", 21), Student.class);

        // Теперь проверяем, можем ли мы получить студентов в диапазоне возрастов
        ResponseEntity<List<Student>> response = restTemplate.exchange("http://localhost:" + port + "/student/age/range?min=19&max=21", HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {});
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().size()).isEqualTo(2);
    }

    @Test
    void testGetStudentsByFaculty() {
        ResponseEntity<Faculty> newFacultyResponseEntity =
                restTemplate.postForEntity("http://localhost:" + port + "/faculty", new Faculty("magic", "blue"), Faculty.class);
        Assertions.assertThat(newFacultyResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        restTemplate.postForEntity("http://localhost:" + port + "/student/faculty/" + newFacultyResponseEntity.getBody().getId() + "/student", new Student("John", 20), Student.class);
        ResponseEntity<List<Student>> response = restTemplate.exchange("http://localhost:" + port + "/student/faculty/" + newFacultyResponseEntity.getBody().getId(), HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {});
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().get(0).getName()).isEqualTo("John");
    }

}
