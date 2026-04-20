package com.primetrade.controller;

import com.primetrade.model.Task;
import com.primetrade.model.User;
import com.primetrade.repository.TaskRepository;
import com.primetrade.repository.UserRepository;
import com.primetrade.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<?> getAllTasks(@RequestHeader("Authorization") String authHeader) {
        try {
            System.out.println("📋 Fetching tasks...");
            
            String token = authHeader.replace("Bearer ", "");
            String email = jwtUtil.getEmailFromToken(token);
            
            System.out.println("👤 Email from token: " + email);
            
            User user = userRepository.findByEmail(email).orElse(null);
            
            if (user == null) {
                System.out.println("❌ User not found!");
                return ResponseEntity.status(401).body("User not found");
            }
            
            System.out.println("✅ User found: " + user.getId());
            
            List<Task> tasks = taskRepository.findByUserId(user.getId());
            
            System.out.println("📝 Found " + tasks.size() + " tasks");
            
            return ResponseEntity.ok(tasks);
            
        } catch (Exception e) {
            System.out.println("💥 Error fetching tasks: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task task, @RequestHeader("Authorization") String authHeader) {
        try {
            System.out.println("➕ Creating new task...");
            System.out.println("📝 Title: " + task.getTitle());
            
            String token = authHeader.replace("Bearer ", "");
            String email = jwtUtil.getEmailFromToken(token);
            
            User user = userRepository.findByEmail(email).orElse(null);
            
            if (user == null) {
                System.out.println("❌ User not found!");
                return ResponseEntity.status(401).body("User not found");
            }
            
            task.setUserId(user.getId());
            task.setCreatedAt(LocalDateTime.now());
            task.setUpdatedAt(LocalDateTime.now());
            task.setStatus("PENDING");
            
            Task savedTask = taskRepository.save(task);
            
            System.out.println("✅ Task created with ID: " + savedTask.getId());
            
            return ResponseEntity.ok(savedTask);
            
        } catch (Exception e) {
            System.out.println("💥 Error creating task: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable String id, @RequestBody Task taskUpdate, @RequestHeader("Authorization") String authHeader) {
        try {
            System.out.println("🔄 Updating task: " + id);
            
            Task task = taskRepository.findById(id).orElse(null);
            
            if (task == null) {
                return ResponseEntity.status(404).body("Task not found");
            }
            
            if (taskUpdate.getTitle() != null) task.setTitle(taskUpdate.getTitle());
            if (taskUpdate.getDescription() != null) task.setDescription(taskUpdate.getDescription());
            if (taskUpdate.getStatus() != null) task.setStatus(taskUpdate.getStatus());
            task.setUpdatedAt(LocalDateTime.now());
            
            Task updatedTask = taskRepository.save(task);
            
            System.out.println("✅ Task updated successfully!");
            
            return ResponseEntity.ok(updatedTask);
            
        } catch (Exception e) {
            System.out.println("💥 Error updating task: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable String id) {
        try {
            System.out.println("🗑️ Deleting task: " + id);
            
            taskRepository.deleteById(id);
            
            System.out.println("✅ Task deleted successfully!");
            
            return ResponseEntity.ok("Task deleted");
            
        } catch (Exception e) {
            System.out.println("💥 Error deleting task: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}