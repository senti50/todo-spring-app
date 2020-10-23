package pl.senti.todoapp.logic;

import pl.senti.todoapp.TaskConfigurationProperties;
import pl.senti.todoapp.model.*;
import pl.senti.todoapp.model.projection.GroupReadModel;
import pl.senti.todoapp.model.projection.GroupTaskWriteModel;
import pl.senti.todoapp.model.projection.GroupWriteModel;
import pl.senti.todoapp.model.projection.ProjectWriteModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {
    private final ProjectRepository repository;
    private final TaskGroupRepository taskGroupRepository;
    private final TaskConfigurationProperties config;
    private TaskGroupService taskGroupService;


    public ProjectService(final ProjectRepository repository, final TaskGroupRepository taskGroupRepository, TaskGroupService taskGroupService, final TaskConfigurationProperties config) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.taskGroupService = taskGroupService;
        this.config = config;
    }

    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project save(final ProjectWriteModel toSave){
        return repository.save(toSave.toProject());
    }

    public Project createProject(final Project toSave) {
        return repository.save(toSave);
    }

    public GroupReadModel createGroup(long projectId, LocalDateTime deadline) {
        if (!config.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        return repository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getSteps().stream()
                                    .map(projectStep -> {
                                        var task=new GroupTaskWriteModel();
                                        task.setDescription(projectStep.getDescription());
                                        task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                                    return task;
                                    }
                                    ).collect(Collectors.toList())
                    );
                   return taskGroupService.crateGroup(targetGroup,project);

                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
    }

}
