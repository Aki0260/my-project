package com.example.spring_traing.Repository;

import com.example.spring_traing.Entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
