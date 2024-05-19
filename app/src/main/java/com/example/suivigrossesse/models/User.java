package com.example.suivigrossesse.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class User implements Serializable {

    private String name,email,address,phone;
    private LocalDate dateConception;

    public User() {
    }

    public User(String name, String email, String address, String phone) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public User(String name, String email, String address, String phone, LocalDate dateConception) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.dateConception = dateConception;
    }

    public LocalDate getDateConception() {
        return dateConception;
    }

    public void setDateConception(LocalDate dateConception) {
        this.dateConception = dateConception;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", dateConception=" + dateConception +
                '}';
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int semaineGrossesse() {
        // Date de conception
//        LocalDate dateDeConception = LocalDate.of(2024, 5, 12);
//        dateConception = LocalDate.of(2024, 2, 27);

        // Calcul de la semaine en cours
        LocalDate dateActuelle = LocalDate.now();
        long jours = ChronoUnit.DAYS.between(dateConception, dateActuelle);
        int semaine = (int) (jours / 7);

        return semaine;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String dateAccouchement() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, 39 - this.semaineGrossesse());
        Date dateAccouchement = calendar.getTime();

        // SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        return formatter.format(dateAccouchement);
    }
}
