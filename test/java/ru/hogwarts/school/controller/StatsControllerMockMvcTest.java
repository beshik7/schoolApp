package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.service.StatsService;

import java.util.Arrays;
import java.util.OptionalDouble;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatsController.class)
public class StatsControllerMockMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatsService statsService;

    @Test
    public void testGetNamesStartingWithA() throws Exception {
        when(statsService.getNamesStartingWithA()).thenReturn(Arrays.asList("Anna", "Alex"));

        mockMvc.perform(get("/stats/names-starts-with-a"))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"Anna\", \"Alex\"]"));
    }
    @Test
    public void testGetAverageAge() throws Exception {
        when(statsService.getAverageAge()).thenReturn(OptionalDouble.of(25.5));

        mockMvc.perform(get("/stats/average-age"))
                .andExpect(status().isOk())
                .andExpect(content().string("25.5"));
    }
    @Test
    public void testGetLongestFacultyName() throws Exception {
        when(statsService.getLongestFacultyName()).thenReturn("Faculty of Engineering");

        mockMvc.perform(get("/stats/longest-faculty-name"))
                .andExpect(status().isOk())
                .andExpect(content().string("Faculty of Engineering"));
    }
    @Test
    public void testCalculateSum() throws Exception {
        mockMvc.perform(get("/stats/calc-sum"))
                .andExpect(status().isOk())
                .andExpect(content().string("500000500000"));
    }
}
