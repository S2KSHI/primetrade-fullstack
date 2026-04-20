package com.primetrade.repository;

import com.primetrade.model.Task; // Ye line dhyan se check karna
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

// Yahan <Task, String> hona bahut zaroori hai
public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByUserId(String userId);
}