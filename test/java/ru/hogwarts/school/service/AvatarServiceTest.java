package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AvatarServiceTest {

        @InjectMocks
        private AvatarServiceImpl avatarServiceImpl;

        @Mock
        private AvatarRepository avatarRepository;

        @Mock
        private StudentRepository studentRepository;

        @BeforeEach
        public void setup() throws IOException {
            MockitoAnnotations.initMocks(this);
        }
            @Test
            public void testUploadAvatar() throws IOException {
                Long studentId = 1L;
                MultipartFile file = new MockMultipartFile("file", "filename.jpg", "image/jpeg", "some-image".getBytes());

                Student mockStudent = new Student();
                mockStudent.setId(studentId);

                Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(mockStudent));

                Avatar avatar = avatarServiceImpl.uploadAvatar(studentId, file);

                assertNotNull(avatar);
                assertEquals(file.getContentType(), avatar.getMediaType());
                assertTrue(avatar.getFilePath().contains("./avatar"));
                assertEquals(file.getSize(), avatar.getFileSize().longValue());
                assertArrayEquals(file.getBytes(), avatar.getData());
            }
            @Test
        public void testGetAvatarById() {
        Long avatarId = 1L;
        Avatar mockAvatar = new Avatar();
        mockAvatar.setId(avatarId);

        Mockito.when(avatarRepository.findById(avatarId)).thenReturn(Optional.of(mockAvatar));

        Avatar avatar = avatarServiceImpl.getAvatar(avatarId);

        assertEquals(avatarId, avatar.getId());
    }

    @Test
    public void testAvatarNotFound() {
        Long avatarId = 1L;
        Mockito.when(avatarRepository.findById(avatarId)).thenReturn(Optional.empty());

        avatarServiceImpl.getAvatar(avatarId);
    }

    }
