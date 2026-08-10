package com.example.spring_traing.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity //このJavaクラスはDBのテーブルとして扱いますということ
@Table(name = "projects") //JavaのProjectとDBのprojectsテーブルを紐づける
@Getter
@Setter
public class Project {
    @Id //これが主キーであることを示す
    @GeneratedValue(strategy = GenerationType.IDENTITY) //主キーの値を自動生成することを示す
    private Long id;

    private String name;

    private String description;

    private String goal;

    private String motivation;

    private String status;

    private String priority;

    @Column(name = "start_date")
    private LocalDate startDate; //LocalDateは日付のみを扱うクラスで、時間は含まない

    @Column(name = "planned_end_date")
    private LocalDate plannedEndDate; //LocalDateは日付のみを扱うクラスで、時間は含まない

    @Column(name = "actual_end_date")
    private LocalDate actualEndDate;

    @Column(name = "planned_hours")
    private BigDecimal plannedHours; //BigDecimalは小数点を含む数値を扱うクラスで、精度の高い計算が可能

    @Column(name = "created_at")
    private LocalDateTime createdAt; //LocalDateTimeは日付と時間の両方を扱うクラスで、タイムゾーンは含まない

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
