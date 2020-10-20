package pl.senti.todoapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.senti.todoapp.model.Task;
import pl.senti.todoapp.model.TaskRepository;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
public class TaskControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void httpGet_returnsGivenTask() throws Exception {
        // given
        long id=taskRepository.save(new Task("foo", LocalDateTime.now())).getId();
        //when +then
        mockMvc.perform(get("/tasks/"+id))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void httpPost_addTask() throws Exception{
        //given
        String taskJsonStr = "{\"done\":false, \"description\": \"add task\", \"deadline\":\"2020-12-23T23:59:59.999\"}";
        // when + then
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJsonStr))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

}
