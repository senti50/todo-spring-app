package pl.senti.todoapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Project's description must  not be empty")
    private String description;
    @OneToMany(mappedBy = "project")
    private Set<TasksGroup> groups;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "project")
    private Set<ProjectStep> steps;

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

    Set<TasksGroup> getGroups() {
        return groups;
    }

    void setGroups(Set<TasksGroup> groups) {
        this.groups = groups;
    }

    public Set<ProjectStep> getSteps() {
        return steps;
    }

    void setSteps(Set<ProjectStep> steps) {
        this.steps = steps;
    }
}
