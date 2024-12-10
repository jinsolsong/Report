package com.spring.schedule.repository;

import com.spring.schedule.dto.ScheduleResponseDto;
import com.spring.schedule.entity.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
public class JdbcTemplateScheduleRepository implements ScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateScheduleRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public ScheduleResponseDto saveSchedule(Schedule schedule) {

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("schedule").usingGeneratedKeyColumns("id");

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(dateTimeFormatter);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("todo", schedule.getTodo());
        parameters.put("name", schedule.getName());
        parameters.put("password", schedule.getPassword());
        parameters.put("createDate", now);
        parameters.put("updateDate", now);


        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));


        return new ScheduleResponseDto(key.longValue(), schedule.getTodo(), schedule.getName(),now,now);
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules() {

        return jdbcTemplate.query("select * from schedule", scheduleRowMapper());
    }

    @Override
    public Optional<Schedule> findScheduleById(Long id) {

        List<Schedule> result = jdbcTemplate.query("select * from schedule where id = ?", scheduleRowMapperV2(), id);

        return result.stream().findAny();
    }

    @Override
    public Schedule findScheduleByNameOrElseThrow(Long id) {

        List<Schedule> result = jdbcTemplate.query("select * from schedule where id = ?", scheduleRowMapperV2(), id);

        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exists id = " + id));
    }

    @Override
    public int updateSchedule(Long id, String todo, String name) {

        return jdbcTemplate.update("update schedule set todo = ?, name = ? where id = ?", todo, name, id);
    }

    @Override
    public int updateName(Long id, String name) {



        return jdbcTemplate.update("update schedule set name = ? where id = ?", name, id);
    }

    @Override
    public int deleteSchedule(Long id) {

        return jdbcTemplate.update("delete from schedule where id = ?", id);

    }

    private RowMapper<ScheduleResponseDto> scheduleRowMapper(){
        return new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ScheduleResponseDto(
                        rs.getLong("id"),
                        rs.getString("todo"),
                        rs.getString("name"),
                        rs.getString("createDate"),
                        rs.getString("updateDate")
                );

            }
        };
    }

    private RowMapper<Schedule> scheduleRowMapperV2(){
        return new RowMapper<Schedule>() {
            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Schedule(
                        rs.getLong("id"),
                        rs.getString("todo"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("createDate"),
                        rs.getString("updateDate")
                );
            }
        };


    }

}
