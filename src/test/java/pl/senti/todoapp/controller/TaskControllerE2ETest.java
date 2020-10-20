package pl.senti.todoapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import pl.senti.todoapp.model.Task;
import pl.senti.todoapp.model.TaskRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerE2ETest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    @Autowired
    private TaskRepository taskRepository;

    @Test
    void httpGet_returnAllTasks() {
        //given
        int initialSize=taskRepository.findAll().size();
        taskRepository.save(new Task("foo", LocalDateTime.now()));
        taskRepository.save(new Task("bar", LocalDateTime.now()));

        //when
        Task[] result = restTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);
        //then
        assertThat(result).hasSize(initialSize+2);

    }
    @Test
    void httpGet_returnsGivenTask() {
        // given
        taskRepository.save(new Task("test task", LocalDateTime.now()));

        // when
        Task result = restTemplate.getForObject("http://localhost:" + port + "/tasks/2", Task.class);

        // then
        assertThat(result).hasFieldOrPropertyWithValue("description", "test task");
    }
}