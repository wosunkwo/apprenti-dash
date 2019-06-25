package com.example.teamboolean.apprentidash;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.*;

public class DayTest {

    @Test
    public void calculateDailyHours() {
        LocalDateTime startHour = LocalDate.now().atTime(9, 0);
        LocalDateTime endHour = LocalDate.now().atTime(18, 0);
        LocalDateTime lunchStart = LocalDate.now().atTime(12, 0);
        LocalDateTime lunchEnd = LocalDate.now().atTime(13, 0);

        AppUser userTest = new AppUser("myusername", "mypassword", "joe", "sands",
                "mngr");

        Day test = new Day(startHour, endHour, lunchStart, lunchEnd, userTest);

        assertEquals("Duration should be the same", 8, test.calculateDailyHours(), 0.01 );
    }

    @Test
    public void calculateDailyHoursNotExactMinutes() {
        LocalDateTime startHour = LocalDate.now().atTime(9, 0);
        LocalDateTime endHour = LocalDate.now().atTime(18, 30);
        LocalDateTime lunchStart = LocalDate.now().atTime(12, 10);
        LocalDateTime lunchEnd = LocalDate.now().atTime(13, 10);

        AppUser userTest = new AppUser("myusername", "mypassword", "joe", "sands",
                "mngr");

        Day test = new Day(startHour, endHour, lunchStart, lunchEnd, userTest);

        assertEquals("Duration should be the same", 8.5, test.calculateDailyHours(), 0.01 );
    }


}