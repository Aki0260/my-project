package com.example.spring_traing.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;
//DTO（Data Transfer Object）クラスは、データの転送を目的としたシンプルなオブジェクトであり、
// 通常はエンティティクラスとは異なり、ビジネスロジックを持たず、データの構造を定義するために使用されます。
// ProjectUpdateRequestクラスは、プロジェクトの更新リクエストを表すDTOです。
public class ProjectUpdateRequest {

    private String name;

    private String description;

    private String goal;

    private String motivation;

    private String status;

    private String priority;

    private LocalDate startDate;

    private LocalDate plannedEndDate;

    private BigDecimal plannedHours;

    // 以下は、各フィールドのゲッターとセッターです。これらのメソッドは、外部からフィールドの値を取得したり設定したりするために使用されます。
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getPlannedEndDate() {
        return plannedEndDate;
    }

    public void setPlannedEndDate(LocalDate plannedEndDate) {
        this.plannedEndDate = plannedEndDate;
    }

    public BigDecimal getPlannedHours() {
        return plannedHours;
    }

    public void setPlannedHours(BigDecimal plannedHours) {
        this.plannedHours = plannedHours;
    }
}
