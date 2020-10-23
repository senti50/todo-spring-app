package pl.senti.todoapp.model.event;

import pl.senti.todoapp.model.Task;

import java.time.Clock;
import java.time.Instant;

public abstract class TaskEvent {
    public static TaskEvent changed(Task source){
        return source.isDone() ? new TaskDone(source): new TaskUndone(source);
    }

    private long taskId;
    private Instant occurrence;

    TaskEvent(long taskId, Clock clock) {
        this.taskId = taskId;
        this.occurrence=Instant.now(clock);
    }

    public long getTaskId() {
        return taskId;
    }

    public Instant getOccurrence() {
        return occurrence;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+"{" +
                "taskId=" + taskId +
                ", occurrence=" + occurrence +
                '}';
    }
}
