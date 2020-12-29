package com.example.edge.model;

import java.util.List;

public class Edge {
    private Schip schip;
    private String schipNaam;
    private int schipCapaciteit;
    private List<Container> containers;

    public Edge() {
    }

    public Edge(String schipNaam, int schipCapaciteit, List<Container> containers) {
        this.schipNaam = schipNaam;
        this.schipCapaciteit = schipCapaciteit;
        this.containers = containers;
    }

    public Edge(Schip schip, List<Container> containers) {
        this.schip = schip;
        this.containers = containers;
    }

    public Schip getSchip() {
        return schip;
    }

    public void setSchip(Schip schip) {
        this.schip = schip;
    }

    public List<Container> getContainers() {
        return containers;
    }

    public void setContainers(List<Container> containers) {
        this.containers = containers;
    }

    public String getSchipNaam() {
        return schipNaam;
    }

    public void setSchipNaam(String schipNaam) {
        this.schipNaam = schipNaam;
    }

    public int getSchipCapaciteit() {
        return schipCapaciteit;
    }

    public void setSchipCapaciteit(int schipCapaciteit) {
        this.schipCapaciteit = schipCapaciteit;
    }
}
