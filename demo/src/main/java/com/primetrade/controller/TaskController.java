package com.primetrade.controller;

import com.primetrade.model.Task;
import com.primetrade.model.User;
import com.primetrade.service.TaskService;
import com.primetrade.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return ResponseEntity.status(401).build();
        
        return ResponseEntity.ok(taskService.getTasksByUserId(user.getId()));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return ResponseEntity.status(401).build();
        
        task.setUserId(user.getId());
        return ResponseEntity.ok(taskService.createTask(task));
    }
}