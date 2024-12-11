package com.spring.schedule.service;

import com.spring.schedule.dto.ScheduleRequestDto;
import com.spring.schedule.dto.ScheduleResponseDto;

import java.util.List;

public interface ScheduleService {

    ScheduleResponseDto saveSchedule(ScheduleRequestDto dto);

    List<ScheduleResponseDto> findAllSchedules();

    ScheduleResponseDto findScheduleById(Long id);

    ScheduleResponseDto updateSchedule(Long id, String todo, String name, String password, String updateDate);

    ScheduleResponseDto updateName(Long id, String todo, String name, String password, String updateDate);

    ScheduleResponseDto deleteSchedule(Long id, String password);


}
