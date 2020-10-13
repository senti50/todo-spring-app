package pl.senti.todoapp.logic;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import pl.senti.todoapp.TaskConfigurationProperties;
import pl.senti.todoapp.model.TaskGroupRepository;
import pl.senti.todoapp.model.TaskRepository;
import pl.senti.todoapp.model.TasksGroup;
import pl.senti.todoapp.model.projection.GroupReadModel;
import pl.senti.todoapp.model.projection.GroupWriteModel;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
public class TaskGroupService {
    private final TaskGroupRepository repository;
    private final TaskRepository taskRepository;


    public TaskGroupService(final TaskGroupRepository repository, final TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;

    }

    public GroupReadModel crateGroup(GroupWriteModel source) {
        TasksGroup result = repository.save(source.toGroup());
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(long groupId) {
        if (taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)) {
            throw new IllegalStateException("Group has undone tasks.Done all the task first");
        }

        TasksGroup result = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("TaskGroup with given id not found"));
        result.setDone(!result.isDone());
        repository.save(result);
    }
}
