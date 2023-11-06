package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
@Service
public class StudentNamePrintingImpl implements Printing {
    private final StudentRepository studentRepository;

    public StudentNamePrintingImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void printNamesWithoutSync() {
        List<Student> students = studentRepository.findAll();
        // Основной поток
        System.out.println(students.get(0).getName());
        System.out.println(students.get(1).getName());

        // Параллельный поток 1
        new Thread(() -> {
            System.out.println(students.get(2).getName());
            System.out.println(students.get(3).getName());
        }).start();

        // Параллельный поток 2
        new Thread(() -> {
            System.out.println(students.get(4).getName());
            System.out.println(students.get(5).getName());
        }).start();
    }

    @Override
    public void printNamesWithSync() {
        List<Student> students = studentRepository.findAll();

        // Основной поток
        printNameSynchronized(students.get(0));
        printNameSynchronized(students.get(1));

        // Параллельный поток 1
        new Thread(() -> {
            printNameSynchronized(students.get(2));
            printNameSynchronized(students.get(3));
        }).start();

        // Параллельный поток 2
        new Thread(() -> {
            printNameSynchronized(students.get(4));
            printNameSynchronized(students.get(5));
        }).start();
    }

    private synchronized void printNameSynchronized(Student student) {
        System.out.println(student.getName());
    }



    }
