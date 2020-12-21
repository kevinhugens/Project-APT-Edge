package com.example.edge.model;

import java.util.List;

public class Edge {
    private Schip schip;
    private String schipNaam;
    private String schipCapaciteit;
    private List<Container> containers;

    public Edge() {
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
}
