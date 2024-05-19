package com.example.suivigrossesse.models;

public class  Appointment {

    private String id;
    private boolean booked;
    private boolean visitDone;
    private String date;

    public Appointment() {
    }

    public Appointment(String id, boolean booked, boolean done, String date) {
        this.id = id;
        this.booked = booked;
        this.visitDone = done;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public boolean isBooked() {
        return booked;
    }

    public String getDate() {
        return date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isVisitDone() {
        return visitDone;
    }

    public void setVisitDone(boolean visitDone) {
        this.visitDone = visitDone;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id='" + id + '\'' +
                ", booked=" + booked +
                ", done=" + visitDone +
                ", date=" + date +
                '}';
    }
}
