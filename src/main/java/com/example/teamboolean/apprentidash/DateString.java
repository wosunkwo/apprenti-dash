package com.example.teamboolean.apprentidash;

import org.springframework.stereotype.Service;


public class DateString {
    String day;
    String date;
    String timeIn;
    String timeOut;
    double lunch;
    double dailyHours;

    public DateString(String day, String date, String timeIn, String timeOut, double lunch, double dailyHours) {
        this.day = day;
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.lunch = lunch;
        this.dailyHours = dailyHours;
    }

    public String toString(){
        return day+"," + date+","+ timeIn+","+ timeOut+","+ lunch+"," + dailyHours;
    }

    public String getDay() {
        return day;
    }

    public String getDate() {
        return date;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public double getLunch() {
        return lunch;
    }

    public double getDailyHours() {
        return dailyHours;
    }
}
