package pl.senti.todoapp.model.projection;

import pl.senti.todoapp.model.Task;
import pl.senti.todoapp.model.TasksGroup;

import java.time.LocalDateTime;

public class GroupTaskWriteModel {
    private String description;
    private LocalDateTime deadline;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Task toTask(final TasksGroup group){
        return new Task(description,deadline,group);

    }
}
