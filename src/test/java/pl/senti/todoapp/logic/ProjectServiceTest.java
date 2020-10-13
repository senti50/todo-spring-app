package pl.senti.todoapp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.senti.todoapp.TaskConfigurationProperties;
import pl.senti.todoapp.model.*;
import pl.senti.todoapp.model.projection.GroupReadModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw IllegalStateExceptions when configured to allow just 1 group and the other undone group exists")
    void createGroup_noMultipleGroupsConfig_And_undoneGroupExists_openGroups_throwsIllegalStateExceptions() {
        //given
        var mockGroupRepository = groupRepositoryReturning(true);
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(false);
        //system under test
        var toTest = new ProjectService(null, mockGroupRepository, mockConfig);
        // when
        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("undone group");
    }


    @Test
    @DisplayName("should throw IllegalArgumentExceptions when configuration is ok and no projects for given id")
    void createGroup_configurationOk_And_noProjects_throwsIllegalArgumentExceptions() {
        //given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyLong())).thenReturn(Optional.empty());
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        //system under test
        var toTest = new ProjectService(mockRepository, null, mockConfig);
        // when
        var exception = catchThrowable(() -> toTest.createGroup(0L, LocalDateTime.now()));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should throw IllegalArgumentExceptions when configured to allow just 1 group and no groups and no projects for given id")
    void createGroup_noMultipleGroupsConfig_And_noUndoneGroupExists_noProjects_throwsIllegalArgumentExceptions() {
        //given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyLong())).thenReturn(Optional.empty());
        //and
        var mockGroupRepository = groupRepositoryReturning(false);
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        //system under test
        var toTest = new ProjectService(mockRepository, mockGroupRepository, mockConfig);
        // when
        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should create new group from project")
    void createGroup_configurationOk_existingProject_createAndSavesGroup() {
        //given
        var today = LocalDate.now().atStartOfDay();
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        //and
        var project= projectWith("bar", Set.of(-1, -2));
        var mockRepository = mock(ProjectRepository.class);
//        when(mockRepository.findById(anyLong()))
//                .thenReturn(Optional.of(project));
        doReturn(Optional.of(project)).when(mockRepository).findById(anyLong());
        //and
        InMemoryGroupRepository inMemoryGroupRepo = inMemoryGroupRepository();
        long countBeforeCall = inMemoryGroupRepo.count();
        //system under test
        var toTest = new ProjectService(mockRepository, inMemoryGroupRepo, mockConfig);
        // when
        GroupReadModel result = toTest.createGroup(1, today);
        // then
        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task->task.getDescription().equals("foo"));
        assertThat(countBeforeCall + 1).isEqualTo(inMemoryGroupRepo.count());


    }


    private TaskGroupRepository groupRepositoryReturning(final boolean result) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyLong())).thenReturn(result);
        return mockGroupRepository;
    }

    private TaskConfigurationProperties configurationReturning(boolean result) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }

    private Project projectWith(String projectDescription, Set<Integer> daysToDeadline) {
        Set<ProjectStep> steps=daysToDeadline.stream()
                .map(days -> {
                    var step = mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("foo");
                    when(step.getDaysToDeadline()).thenReturn(days);
                    return step;
                })
                .collect(Collectors.toSet());
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        when(result.getSteps()).thenReturn(steps);
        return result;
    }

    private InMemoryGroupRepository inMemoryGroupRepository() {
        return new InMemoryGroupRepository();
    }

    private static class InMemoryGroupRepository implements TaskGroupRepository {
        private Map<Long, TasksGroup> map = new HashMap<>();
        private Long index = 0L;

        public long count() {
            return map.values().size();
        }

        @Override
        public List<TasksGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TasksGroup> findById(Long id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TasksGroup save(TasksGroup entity) {
            if (entity.getId() == 0L) {
                try {
                    var field=TasksGroup.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, ++index);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            map.put(entity.getId(), entity);

            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(Long projectId) {
            return map.values().stream()
                    .filter(tasksGroup -> !tasksGroup.isDone())
                    .anyMatch(tasksGroup -> tasksGroup.getProject() != null && tasksGroup.getProject().getId() == projectId);
        }
    }

}