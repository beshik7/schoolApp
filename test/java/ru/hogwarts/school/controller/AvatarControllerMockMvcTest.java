package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.AvatarServiceImpl;
import ru.hogwarts.school.service.StudentServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

    @WebMvcTest(AvatarController.class)
    public class AvatarControllerMockMvcTest {
        @Autowired
        private MockMvc mockMvc;
        @MockBean
        private AvatarServiceImpl avatarService;

        @MockBean
        private StudentServiceImpl studentService;

        @MockBean
        private FacultyRepository facultyRepository;

        @Test
        public void testGetPaginatedAvatars() throws Exception {
            List<Avatar> avatars = new ArrayList<>();
            avatars.add(new Avatar());
            avatars.add(new Avatar());

            when(avatarService.getPaginatedAvatars(0, 2)).thenReturn(avatars);

            mockMvc.perform(get("/avatars?page=0&size=2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)));

        }

    }
