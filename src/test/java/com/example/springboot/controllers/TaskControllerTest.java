//package com.example.springboot.controllers;
//
//import com.example.springboot.models.entities.*;
//import com.example.springboot.services.TaskService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultMatcher;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.HashSet;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//
//@WebMvcTest(controllers = TaskController.class)
//class TaskControllerTest {
//
//    @Mock
//    private TaskService taskService;  // Mock the TaskService with @MockBean
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void createTask() throws Exception {
//        // Create a mock Task object
////        Task savedTask = new Task(1L, "Task Title", "Task Description", null, TaskStatus.NEW, null, new HashSet<>());
//
//        // Mock the service method call
////        when(taskService.createTask(Mockito.any(Task.class), null)).thenReturn(savedTask);
//
//        // Perform the POST request to create a task and validate the response
//        mockMvc.perform(post("/api/tasks")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"title\":\"Task Title\",\"description\":\"Task Description\",\"status\":\"NEW\",\"userId\":1,\"projectId\":1}"))
//                .andExpect(status().isCreated())  // Expect 201 Created status
//                .andExpect(jsonPath("$.id").value(1L))  // Expect id to be 1
//                .andExpect(jsonPath("$.title").value("Task Title"))  // Expect title
//                .andExpect(jsonPath("$.status").value("NEW"));  // Expect status to be NEW
//    }
//}