package com.example.springboot.controllers;

import com.example.springboot.services.BoardService;
import com.example.springboot.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;
    @Mock
    private BoardService boardService;
    @InjectMocks
    private BoardController boardController;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createTaskAndReturnStatus200() throws Exception {


    }
}
