package pl.senti.todoapp.model;

import pl.senti.todoapp.model.event.TaskEvent;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Task's description must  not be empty")
    private String description;
    private boolean done;
    private LocalDateTime deadline;
    @Embedded
    private final Audit audit=new Audit();
    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TasksGroup group;

    Task() {
    }

    public Task(String description,LocalDateTime deadline){
        this(description, deadline,null);
    }
    public Task(String description,LocalDateTime deadline,TasksGroup group){
        this.description=description;
        this.deadline=deadline;
        if (group!=null){
            this.group=group;
        }
    }
    public long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public TaskEvent toggle() {
        this.done = !this.done;
        return TaskEvent.changed(this);
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    TasksGroup getGroup() {
        return group;
    }

    void setGroup(TasksGroup group) {
        this.group = group;
    }

    public void updateFrom(final Task source) {
        description = source.description;
        done = source.done;
        deadline = source.deadline;
        group= source.group;
    }


}
