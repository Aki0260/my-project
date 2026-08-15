package com.example.spring_traing.Service;

import com.example.spring_traing.Entity.Todo;
import com.example.spring_traing.Repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    // ToDo一覧取得
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    // ToDo取得
    public Todo getTodo(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ToDoが見つかりません。"));
    }

    // ToDo作成
    public Todo createTodo(Todo todo) {
        LocalDateTime now = LocalDateTime.now();

        todo.setCreatedAt(now);
        todo.setUpdatedAt(now);

        return todoRepository.save(todo);
    }

    // ToDo更新
    public Todo updateTodo(Long id, Todo todo) {
        Todo existingTodo = getTodo(id);

        existingTodo.setProjectId(todo.getProjectId());
        existingTodo.setTitle(todo.getTitle());
        existingTodo.setPlannedHours(todo.getPlannedHours());
        existingTodo.setStatus(todo.getStatus());
        existingTodo.setDueDate(todo.getDueDate());
        existingTodo.setMemo(todo.getMemo());
        existingTodo.setUpdatedAt(LocalDateTime.now());

        return todoRepository.save(existingTodo);
    }

    // ToDo削除
    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }
}