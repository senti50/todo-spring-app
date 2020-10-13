package pl.senti.todoapp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.senti.todoapp.model.Task;
import pl.senti.todoapp.model.TaskRepository;

import java.util.List;

@Repository
interface SqlTaskRepository extends TaskRepository,JpaRepository<Task,Long> {
    @Override
    @Query(nativeQuery = true,value = "select count(*) >0 from tasks where id=:id")
    boolean existsById(@Param("id") Long id);

    @Override
    boolean existsByDoneIsFalseAndGroup_Id(Long groupId);
}
