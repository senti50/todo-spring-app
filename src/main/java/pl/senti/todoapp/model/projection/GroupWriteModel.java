package pl.senti.todoapp.model.projection;

import pl.senti.todoapp.model.TasksGroup;

import java.util.Set;
import java.util.stream.Collectors;

public class GroupWriteModel {
    private String description;
    private Set<GroupTaskWriteModel> tasks;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<GroupTaskWriteModel> getTasks() {
        return tasks;
    }

    public void setTasks(Set<GroupTaskWriteModel> tasks) {
        this.tasks = tasks;
    }

    public TasksGroup toGroup(){
    var result=new TasksGroup();
    result.setDescription(description);
    result.setTasks(
            tasks.stream()
            .map(source->source.toTask(result))
            .collect(Collectors.toSet())
    );
    return result;
    }
}
