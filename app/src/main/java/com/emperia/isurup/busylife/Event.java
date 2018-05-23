package com.emperia.isurup.busylife;

/**
 * Created by IsuruP on 4/15/2018.
 */

public class Event {
    /***
     * declare & initialize variables
     */
    private int id;
    private int year;
    private int month;
    private int day;
    private String date;
    private String time;
    private int hour;
    private int minute;
    private String title;
    private String details;
    private double calTime;

    /**
     *
     * @param title
     * @param details
     * @param date
     * @param time
     * @param calTime
     */
    public Event(String title, String details, String date, String time, double calTime) {
        this.title = title;
        this.details = details;
        this.date = date;
        this.time = time;
        this.calTime = calTime;
    }

    /**
     * generated getters and setters
     * @return
     */
    public double getCalTime() {
        return calTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
