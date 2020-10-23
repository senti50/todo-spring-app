package pl.senti.todoapp.model;


import java.util.List;
import java.util.Optional;

public interface TaskGroupRepository {
    List<TasksGroup> findAll();

    Optional<TasksGroup> findById(Long id);

    TasksGroup save(TasksGroup entity);

    boolean existsByDoneIsFalseAndProject_Id(Long projectId);

    boolean existsByDescription(String description);
}
