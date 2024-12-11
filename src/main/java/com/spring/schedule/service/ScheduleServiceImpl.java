package com.spring.schedule.service;

import com.spring.schedule.dto.ScheduleRequestDto;
import com.spring.schedule.dto.ScheduleResponseDto;
import com.spring.schedule.entity.Schedule;
import com.spring.schedule.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {


    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }


    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto) {

        //요청받은 데이터로 스케줄 생성. 생성ID는 없음
        Schedule schedule = new Schedule(dto.getTodo(), dto.getName(), dto.getPassword());

        //데이터베이스에 저장

        return scheduleRepository.saveSchedule(schedule);
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules() {

        return scheduleRepository.findAllSchedules();


    }

    @Override
    public ScheduleResponseDto findScheduleById(Long id) {

        Schedule schedule = scheduleRepository.findScheduleByNameOrElseThrow(id);

        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    @Override
    public ScheduleResponseDto updateSchedule(Long id, String todo, String name, String password, String updateDate) {

        if (todo == null || name == null || password == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The todo, password and name are required values.");
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(dateTimeFormatter);

        int updatedRow = scheduleRepository.updateSchedule(id, todo, name, now);

        if (updatedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }


        Schedule schedule = scheduleRepository.findScheduleByNameOrElseThrow(id);

        if (!schedule.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid password");
        }

        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    @Override
    public ScheduleResponseDto updateName(Long id, String todo, String name, String password, String updateDate) {

        if (todo != null || name == null || password == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The todo, password and name are required values.");
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(dateTimeFormatter);

        int updateRow = scheduleRepository.updateName(id, name, now); // 수정 now <<

        if (updateRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }

        Schedule schedule = scheduleRepository.findScheduleByNameOrElseThrow(id);

        if (!schedule.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid password");
        }


        return new ScheduleResponseDto(schedule);

    }

    @Override
    public ScheduleResponseDto deleteSchedule(Long id, String password) {

        if (password == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password required values.");
        }

        Schedule schedule = scheduleRepository.findScheduleByNameOrElseThrow(id);

        if (!schedule.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid password");
        }

        //DB에서 저장된 비밀번호조회
        //가져온거랑 입력값 비교 다르면 예외 같으면 실행 출력

        int deleteRow = scheduleRepository.deleteSchedule(id);

        if (deleteRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);

        }
        return null;
    }

}
