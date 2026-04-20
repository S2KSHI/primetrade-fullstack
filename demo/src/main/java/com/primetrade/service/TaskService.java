package com.primetrade.service;

import com.primetrade.model.Task;
import com.primetrade.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getTasksByUserId(String userId) {
        return taskRepository.findByUserId(userId);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(String taskId) {
        taskRepository.deleteById(taskId);
    }
}