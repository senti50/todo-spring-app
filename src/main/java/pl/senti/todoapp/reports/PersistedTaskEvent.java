package pl.senti.todoapp.reports;

import pl.senti.todoapp.model.event.TaskEvent;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "tasks_events")
class PersistedTaskEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    long taskId;
    String name;
    LocalDateTime occurrence;

    PersistedTaskEvent() {
    }

    PersistedTaskEvent(TaskEvent source) {
        taskId=source.getTaskId();
        name=source.getClass().getSimpleName();
        occurrence=LocalDateTime.ofInstant(source.getOccurrence(), ZoneId.systemDefault());
    }

}
