package pl.senti.todoapp.reports;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface PersistedTaskEventRepository extends JpaRepository<PersistedTaskEvent,Long> {
List<PersistedTaskEvent> findByTaskId(long taskId);
}
