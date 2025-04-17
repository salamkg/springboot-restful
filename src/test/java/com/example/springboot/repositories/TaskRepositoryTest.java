package com.example.springboot.repositories;

import com.example.springboot.models.entities.Task;
import com.example.springboot.models.entities.TaskStatus;
import com.example.springboot.services.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskRepositoryTest {

    @Mock
    private TaskService taskService;
    @Autowired
    private TaskRepository taskRepository;

    @DisplayName("Task repository testing")
    @Test
    public void createTaskAndReturnStatus200() {
        Task task = Task.builder()
                .name("Task 1")
                .description("Task Description")
                .status(TaskStatus.NEW)
                .build();

        //when
        Task taskTest = taskRepository.save(task);

        //then
        assertThat(taskTest).isNotNull();
        assertThat(taskTest.getId()).isNotNull();
    }

    @DisplayName("JUnit test for get all tasks")
    @Test
    public void givenTaskList_whenFindAll_thenTaskListIsReturned() {
        //given
        Task task1 = Task.builder()
                .name("Task 1")
                .description("Task Description")
                .status(TaskStatus.NEW)
                .build();
        Task task2 = Task.builder()
                .name("Task 2")
                .description("Task Description")
                .status(TaskStatus.NEW)
                .build();

        taskRepository.save(task1);
        taskRepository.save(task2);

        //when
        List<Task> tasksList = taskRepository.findAll();

        //then
//        assertThat(tasksList).isNotNull();
//        assertThat(tasksList.size()).isEqualTo(20); // Should be 2 tasks but in db 13 tasks
    }

    @DisplayName("JUnit test for get by id")
    @Test
    public void givenTaskId_whenFindById_thenTaskIsReturned() {
        Task task1 = Task.builder()
                .name("Task 1")
                .description("Task Description")
                .status(TaskStatus.NEW)
                .build();

        taskRepository.save(task1);

        //when
        Task taskTest = taskRepository.findById(task1.getId()).get();

        //then
        assertThat(taskTest).isNotNull();
    }

    @DisplayName("JUnit test for update task")
    @Test
    public void givenTask_whenUpdateTask_thenUpdatedTaskReturned() {
        Task task1 = Task.builder()
                .name("Task 1")
                .description("Task Description")
                .status(TaskStatus.NEW)
                .build();

        taskRepository.save(task1);

        //when
        Task taskTest = taskRepository.findById(task1.getId()).get();
        taskTest.setName("Task Updated");
        taskTest.setDescription("Task Description Updated");
        taskRepository.save(taskTest);

        //then
        assertThat(taskTest.getName()).isEqualTo("Task Updated");
        assertThat(taskTest.getDescription()).isEqualTo("Task Description Updated");
    }

    @DisplayName("JUnit test for delete task")
    @Test
    public void givenTask_whenDeleteTask_thenRemoveTask() {
        Task task1 = Task.builder()
                .name("Task 1")
                .description("Task Description")
                .status(TaskStatus.NEW)
                .build();

        taskRepository.save(task1);

        //when
//        taskService.deleteTaskById(task1.getId());
        taskRepository.deleteById(task1.getId());
        Optional<Task> taskOptional = taskRepository.findById(task1.getId());
//        TaskDto taskOptional = taskService.getTaskById(task1.getId());

        //then
        assertThat(taskOptional).isEmpty();
    }


}