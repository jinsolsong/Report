package com.spring.schedule.dto;

import com.spring.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class ScheduleResponseDto {

    private Long id;
    private String todo;
    private String name;
    private String createDate;
    private String updateDate;


    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.todo = schedule.getTodo();
        this.name = schedule.getName();
        this.createDate = schedule.getCreateDate();
        this.updateDate = schedule.getUpdateDate();


    }
}