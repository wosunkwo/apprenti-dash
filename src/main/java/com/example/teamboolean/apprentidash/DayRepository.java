package com.example.teamboolean.apprentidash;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

import java.util.List;

public interface DayRepository extends CrudRepository <Day, Long> {


//    List<Day> findAllByClockIn(LocalDate clockIn);
//    List<Day> findAllByClockOut(LocalDate clockOut);
//    List<Day> findAllByLunchStart(LocalDate lunchStart);
//    List<Day> findAllByLunchEnd(LocalDate lunchEnd);
//    List<Day> findAllByClockInBetween(LocalDate clockIn1, LocalDate clockIn2);
}
