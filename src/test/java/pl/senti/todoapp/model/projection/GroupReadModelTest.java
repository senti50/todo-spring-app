package pl.senti.todoapp.model.projection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.senti.todoapp.model.Task;
import pl.senti.todoapp.model.TasksGroup;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GroupReadModelTest {
    @Test
    @DisplayName("should create null deadline for group when no task deadlines")
    void constructor_noDeadlines_createsNullDeadline(){
        //given
        var source=new TasksGroup();
        source.setDescription("lolo");
        source.setTasks(Set.of(new Task("kama",null)));
        //when
        var result=new GroupReadModel(source);
        //then
        assertThat(result).hasFieldOrPropertyWithValue("deadline",null);
    }

}