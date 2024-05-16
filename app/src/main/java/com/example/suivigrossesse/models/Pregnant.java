package com.example.suivigrossesse.models;

public class Pregnant {

    private String idsem;
    private String imgsem;
    private String detail;

    public Pregnant(String detail, String idsem, String imgsem) {
        this.idsem = idsem;
        this.imgsem = imgsem;
        this.detail = detail;
    }

    public String getIdsem() {
        return idsem;
    }

    public String getImgsem() {
        return imgsem;
    }

    public String getDetail() {
        return detail;
    }

    @Override
    public String toString() {
        return "Suivie Grossesse{" +
                "idsem='" + idsem + '\'' +
                ", imgsem=" + imgsem +
                ", detail=" + detail +
                '}';
    }
}
