package pl.senti.todoapp.model;


import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    List<Project> findAll();

    Optional<Project> findById(Long id);

    Project save(Project entity);



}
