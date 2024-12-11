package com.spring.schedule.repository;

import com.spring.schedule.dto.ScheduleResponseDto;
import com.spring.schedule.entity.Schedule;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {

    ScheduleResponseDto saveSchedule(Schedule schedule);

    List<ScheduleResponseDto> findAllSchedules();

    Optional<Schedule> findScheduleById(Long id);

    Schedule findScheduleByNameOrElseThrow(Long id);

    int updateSchedule(Long id, String todo, String name, String now);

    int updateName(Long id, String name, String updateDate);

    int deleteSchedule(Long id);

}
