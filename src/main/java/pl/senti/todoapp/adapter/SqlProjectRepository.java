package pl.senti.todoapp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.senti.todoapp.model.Project;
import pl.senti.todoapp.model.ProjectRepository;
import pl.senti.todoapp.model.TaskGroupRepository;
import pl.senti.todoapp.model.TasksGroup;

import java.util.List;

@Repository
interface SqlProjectRepository extends ProjectRepository,JpaRepository<Project,Long> {

    @Override
    @Query("select distinct p from Project p join fetch p.steps")
    List<Project> findAll();


}
