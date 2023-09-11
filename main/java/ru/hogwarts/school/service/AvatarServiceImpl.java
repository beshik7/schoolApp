package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exeption.AvatarNotFoundException;
import ru.hogwarts.school.exeption.StudentNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
@Service
public class AvatarServiceImpl implements AvatarService {
    private Logger logger = LoggerFactory.getLogger(AvatarServiceImpl.class);

    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    public AvatarServiceImpl(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }


    @Override
    public Avatar uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        logger.info("Method uploadAvatar invoked for studentId: {}", studentId);
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new StudentNotFoundException("Student not found"));
        Path filePath = Path.of("./avatar", student + "." + getExtension(file.getOriginalFilename()));

        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = file.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW);

                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);

        }
        Avatar avatar = avatarRepository.findByStudentId(studentId).orElse(new Avatar());
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());
        avatarRepository.save(avatar);
        return avatar;
    }

    @Override
    public Avatar getAvatarId(Long id) {
        logger.info("Fetching Avatar by Id: {}", id);
        return avatarRepository.findById(id).orElseThrow(() -> {
            logger.warn("Avatar not found for id: {}", id);
            new AvatarNotFoundException("Avatar not found");
            return new AvatarNotFoundException("Avatar not found");
        });
    }

    @Override
    public Avatar getAvatar(Long avatarId) {
        logger.info("Fetching Avatar by avatarId: {}", avatarId);
        return avatarRepository.findById(avatarId).orElseThrow(() -> {
            logger.warn("Avatar not found for avatarId: {}", avatarId);
            new AvatarNotFoundException("Avatar not found");
            return new AvatarNotFoundException("Avatar not found");
        });
    }

    @Override
    public List<Avatar> getPaginatedAvatars(Integer page, Integer size) {
        logger.debug("Fetching paginated avatars. Page: {}, Size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Avatar> avatars = avatarRepository.findAll(pageable);
        return avatars.stream().toList();


    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);

    }
}
