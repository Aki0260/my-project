package com.example.spring_traing.Service;

import com.example.spring_traing.Entity.WorkLog;
import com.example.spring_traing.Repository.WorkLogRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WorkLogService {

    private final WorkLogRepository workLogRepository;

    public WorkLogService(WorkLogRepository workLogRepository) {
        this.workLogRepository = workLogRepository;
    }

    public List<WorkLog> getWorkLogs(Long todoId) {
        return workLogRepository.findByTodoId(todoId);
    }

    public WorkLog createWorkLog(WorkLog workLog) {
        return workLogRepository.save(workLog);
    }

    public WorkLog updateWorkLog(Long id, WorkLog workLog) {
        WorkLog existingWorkLog = workLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("作業記録が見つかりません"));

        existingWorkLog.setTodoId(workLog.getTodoId());
        existingWorkLog.setWorkDate(workLog.getWorkDate());
        existingWorkLog.setHours(workLog.getHours());

        return workLogRepository.save(existingWorkLog);
    }

    public void deleteWorkLog(Long id) {
        workLogRepository.deleteById(id);
    }

    public BigDecimal calculateActualHours(Long todoId) {
        return workLogRepository.findByTodoId(todoId)
                .stream()
                .map(WorkLog::getHours)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}