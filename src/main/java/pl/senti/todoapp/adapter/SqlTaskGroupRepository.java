package pl.senti.todoapp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.senti.todoapp.model.TasksGroup;
import pl.senti.todoapp.model.TaskGroupRepository;

import java.util.List;

@Repository
interface SqlTaskGroupRepository extends JpaRepository<TasksGroup,Long>, TaskGroupRepository {

    @Override
    @Query("select distinct g from TasksGroup g join fetch g.tasks")
    List<TasksGroup> findAll();

    @Override
    boolean existsByDoneIsFalseAndProject_Id(Long projectId);
}
