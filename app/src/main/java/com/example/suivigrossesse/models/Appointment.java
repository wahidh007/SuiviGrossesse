package com.example.suivigrossesse.models;

public class  Appointment {

    private String id;
    private boolean booked;
    private long date;

    public Appointment(String id, boolean booked, long date) {
        this.id = id;
        this.booked = booked;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public boolean isBooked() {
        return booked;
    }

    public long getDate() {
        return date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id='" + id + '\'' +
                ", booked=" + booked +
                ", date=" + date +
                '}';
    }
}
