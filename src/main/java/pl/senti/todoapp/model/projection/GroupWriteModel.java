package pl.senti.todoapp.model.projection;

import pl.senti.todoapp.model.Project;
import pl.senti.todoapp.model.Task;
import pl.senti.todoapp.model.TasksGroup;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupWriteModel {
    @NotBlank(message = "Task group's description must  not be empty")
    private String description;
    @Valid
    private List<GroupTaskWriteModel> tasks=new ArrayList<>();

    public GroupWriteModel(){
        tasks.add(new GroupTaskWriteModel());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<GroupTaskWriteModel> getTasks() {
        return tasks;
    }

    public void setTasks(List<GroupTaskWriteModel> tasks) {
        this.tasks = tasks;
    }

    public TasksGroup toGroup(final Project project){
    var result=new TasksGroup();
    result.setDescription(description);
    result.setTasks(
            tasks.stream()
            .map(source->source.toTask(result))
            .collect(Collectors.toSet())
    );
    result.setProject(project);
    return result;
    }
}
