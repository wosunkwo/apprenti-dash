package com.example.teamboolean.apprentidash;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Day {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @DateTimeFormat(pattern="yyyy-mm-dd HH:mm:ss")
    LocalDateTime clockIn;
    @DateTimeFormat(pattern="yyyy-mm-dd HH:mm:ss")
    LocalDateTime clockOut;
    @DateTimeFormat(pattern="yyyy-mm-dd HH:mm:ss")
    LocalDateTime lunchStart;
    @DateTimeFormat(pattern="yyyy-mm-dd HH:mm:ss")
    LocalDateTime lunchEnd;

    @ManyToOne
    AppUser user;

    public Day(){}

    public Day(LocalDateTime clockIn, LocalDateTime clockOut, LocalDateTime lunchStart, LocalDateTime lunchEnd, AppUser user) {
        this.clockIn = clockIn;
        this.clockOut = clockOut;
        this.lunchStart = lunchStart;
        this.lunchEnd = lunchEnd;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public LocalDateTime getClockIn() {
        return clockIn;
    }

    public void setClockIn(LocalDateTime clockIn) {
        this.clockIn = clockIn;
    }

    public LocalDateTime getClockOut() {
        return clockOut;
    }

    public void setClockOut(LocalDateTime clockOut) {
        this.clockOut = clockOut;
    }

    public LocalDateTime getLunchStart() {
        return lunchStart;
    }

    public void setLunchStart(LocalDateTime lunchStart) {
        this.lunchStart = lunchStart;
    }

    public LocalDateTime getLunchEnd() {
        return lunchEnd;
    }

    public void setLunchEnd(LocalDateTime lunchEnd) {
        this.lunchEnd = lunchEnd;
    }

    // TODO: methods to calculate



}
