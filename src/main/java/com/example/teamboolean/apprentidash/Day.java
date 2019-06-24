package com.example.teamboolean.apprentidash;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Entity
public class Day {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @DateTimeFormat(pattern="yyyy-mm-dd HH:mm:ss")
    Date clockIn;
    @DateTimeFormat(pattern="yyyy-mm-dd HH:mm:ss")
    Date clockOut;
    @DateTimeFormat(pattern="yyyy-mm-dd HH:mm:ss")
    Date lunchStart;
    @DateTimeFormat(pattern="yyyy-mm-dd HH:mm:ss")
    Date lunchEnd;

    @ManyToOne
    AppUser user;

    public Day(){}

    public Day(Date clockIn, Date clockOut, Date lunchStart, Date lunchEnd, AppUser user) {
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

    public Date getClockIn() {
        return clockIn;
    }

    public void setClockIn(Date clockIn) {
        this.clockIn = clockIn;
    }

    public Date getClockOut() {
        return clockOut;
    }

    public void setClockOut(Date clockOut) {
        this.clockOut = clockOut;
    }

    public Date getLunchStart() {
        return lunchStart;
    }

    public void setLunchStart(Date lunchStart) {
        this.lunchStart = lunchStart;
    }

    public Date getLunchEnd() {
        return lunchEnd;
    }

    public void setLunchEnd(Date lunchEnd) {
        this.lunchEnd = lunchEnd;
    }

    /**
     * Method to calculate daily working hours
     * @return number of hours worked/day
     */
    public int calculateDailyHours(){
        long diffInMillies = Math.abs(clockOut.getTime() - clockIn.getTime() -
                (lunchEnd.getTime() - lunchStart.getTime()));
        return (int)TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    //get date without hours




}
