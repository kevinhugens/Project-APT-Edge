package com.example.edge.model;

public class Container {

    private int id;

    private int schipId;

    private String serieCode;

    private double gewicht;

    private String inhoud;

    private String startLocatie;

    private String eindLocatie;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSchipId() {
        return schipId;
    }

    public void setSchipId(int schipId) {
        this.schipId = schipId;
    }

    public double getGewicht() {
        return gewicht;
    }

    public void setGewicht(double gewicht) {
        this.gewicht = gewicht;
    }

    public String getInhoud() {
        return inhoud;
    }

    public void setInhoud(String inhoud) {
        this.inhoud = inhoud;
    }

    public String getStartLocatie() {
        return startLocatie;
    }

    public void setStartLocatie(String startLocatie) {
        this.startLocatie = startLocatie;
    }

    public String getEindLocatie() {
        return eindLocatie;
    }

    public void setEindLocatie(String eindLocatie) {
        this.eindLocatie = eindLocatie;
    }

    public Container() {

    }

    public String getSerieCode() {
        return serieCode;
    }

    public void setSerieCode(String serieCode) {
        this.serieCode = serieCode;
    }

    public Container(int schipId, String serieCode , double gewicht, String inhoud, String startLocatie, String eindLocatie) {
        this.schipId = schipId;
        this.serieCode = serieCode;
        this.gewicht = gewicht;
        this.inhoud = inhoud;
        this.startLocatie = startLocatie;
        this.eindLocatie = eindLocatie;
    }
}
