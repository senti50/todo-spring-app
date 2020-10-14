package pl.senti.todoapp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.senti.todoapp.model.TaskGroupRepository;
import pl.senti.todoapp.model.TaskRepository;
import pl.senti.todoapp.model.TasksGroup;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName("should throw IllegalStateExceptions when all task in group is undone")
    void toggleGroup_undoneTasks_throwsIllegalStateExceptions() {
        //given
        TaskRepository mockTaskRepository = TaskRepositoryReturning(true);
        //system under test
        var toTest = new TaskGroupService(null, mockTaskRepository);
        //when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("undone tasks");

    }

    private TaskRepository TaskRepositoryReturning(boolean result) {
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyLong())).thenReturn(result);
        return mockTaskRepository;
    }

    @Test
    @DisplayName("should throw IllegalArgumentExceptions when no taskGroup")
    void toggleGroup_AllTasksDone_And_wrongIdTaskGroup_throwsIllegalArgumentExceptions() {
        TaskRepository mockTaskRepository = TaskRepositoryReturning(false);
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyLong())).thenReturn(Optional.empty());
        //system under test
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);
        //when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");

    }

    @Test
    @DisplayName("should  toggle group")
    void  toggleGroup_worksAsExpected(){
        TaskRepository mockTaskRepository = TaskRepositoryReturning(false);
        var group=new TasksGroup();
        var beforeToggle=group.isDone();
        var mockTaskGroup = mock(TaskGroupRepository.class);
        when(mockTaskGroup.findById(anyLong())).thenReturn(Optional.of(group));
        //system under test
        var toTest = new TaskGroupService(mockTaskGroup, mockTaskRepository);
        //when
        toTest.toggleGroup(1);
        //then
        assertThat(group.isDone()).isEqualTo(!beforeToggle);



    }
}