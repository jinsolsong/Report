package com.spring.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Schedule {


    private Long id;
    private String todo;
    private String name;
    private String password;
    private String createDate;
    private String updateDate;

    public Schedule(String todo, String name, String password){
        this.todo = todo;
        this.name = name;
        this.password = password;

    }


}
