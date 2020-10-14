package pl.senti.todoapp.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    List<Task> findAll();
    Optional<Task> findById(Long i);
    Page<Task> findAll(Pageable page);
    boolean existsById(Long id);
    boolean existsByDoneIsFalseAndGroup_Id(Long groupId);
    Task save(Task entity);
    List<Task> findByDone ( boolean done);

}
