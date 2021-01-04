package com.example.edge.model;

public class Schip {
    private int id;

    private String naam;
    private int capaciteit;
    private String startLocatie;
    private String eindLocatie;
    private int rederijId;

    public Schip() {
    }

    public Schip(String naam, int capaciteit, String startLocatie, String eindLocatie, int rederijId) {
        this.naam = naam;
        this.capaciteit = capaciteit;
        this.startLocatie = startLocatie;
        this.eindLocatie = eindLocatie;
        this.rederijId = rederijId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
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

    public int getCapaciteit() {
        return capaciteit;
    }

    public void setCapaciteit(int capaciteit) {
        this.capaciteit = capaciteit;
    }

    public int getRederijId() {
        return rederijId;
    }

    public void setRederijId(int rederijId) {
        this.rederijId = rederijId;
    }
}
