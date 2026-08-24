package com.example.spring_traing.controller;

import com.example.spring_traing.Entity.WorkLog;
import com.example.spring_traing.Service.WorkLogService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/todos/{todoId}/work-logs")
public class WorkLogController {

    private final WorkLogService workLogService;

    public WorkLogController(WorkLogService workLogService) {
        this.workLogService = workLogService;
    }

    @GetMapping
    public List<WorkLog> getWorkLogs(@PathVariable Long todoId) {
        return workLogService.getWorkLogs(todoId);
    }

    @PostMapping
    public WorkLog createWorkLog(
            @PathVariable Long todoId,
            @RequestBody WorkLog workLog) {

        workLog.setTodoId(todoId);
        workLog.setCreatedAt(LocalDateTime.now());
        workLog.setUpdatedAt(LocalDateTime.now());

        return workLogService.createWorkLog(workLog);
    }

    @PutMapping("/{id}")
    public WorkLog updateWorkLog(
            @PathVariable Long todoId,
            @PathVariable Long id,
            @RequestBody WorkLog workLog) {

        workLog.setTodoId(todoId);
        workLog.setUpdatedAt(LocalDateTime.now());

        return workLogService.updateWorkLog(id, workLog);
    }

    @DeleteMapping("/{id}")
    public void deleteWorkLog(
            @PathVariable Long todoId,
            @PathVariable Long id) {

        workLogService.deleteWorkLog(id);
    }

    @GetMapping("/actual-hours")
    public BigDecimal getActualHours(@PathVariable Long todoId) {
        return workLogService.calculateActualHours(todoId);
    }
}