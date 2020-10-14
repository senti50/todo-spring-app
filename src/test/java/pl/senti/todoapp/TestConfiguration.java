package pl.senti.todoapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.senti.todoapp.model.Task;
import pl.senti.todoapp.model.TaskRepository;

import java.util.*;

@Configuration
class TestConfiguration {
    @Bean
    @Profile("integration")
    TaskRepository testRepository() {
        return new TaskRepository() {
            private final Map<Long, Task> tasks = new HashMap<>();

            @Override
            public List<Task> findAll() {
                return new ArrayList<>(tasks.values());
            }

            @Override
            public Optional<Task> findById(Long i) {
                return Optional.ofNullable(tasks.get(i));
            }

            @Override
            public Page<Task> findAll(Pageable page) {
                return null;
            }

            @Override
            public boolean existsById(Long id) {
                return tasks.containsKey(id);
            }

            @Override
            public boolean existsByDoneIsFalseAndGroup_Id(Long groupId) {
                return false;
            }

            @Override
            public Task save(Task entity) {
                return tasks.put((long) (tasks.size() + 1), entity);
            }

            @Override
            public List<Task> findByDone(boolean done) {
                return null;
            }
        };
    }
}
