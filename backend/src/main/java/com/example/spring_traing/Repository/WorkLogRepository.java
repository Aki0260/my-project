package com.example.spring_traing.Repository;

import com.example.spring_traing.Entity.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {

    List<WorkLog> findByTodoId(Long todoId);
}