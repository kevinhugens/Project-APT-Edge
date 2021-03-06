package com.example.edge.model;

import java.util.List;

public class Edge {
    private Container container;
    private Schip schip;
    private Rederij rederij;
    private List<Schip> schips;
    private List<Container> containers;

    public Edge() {
    }

    public Edge(Schip schip, Rederij rederij ,List<Container> containers) {
        this.schip = schip;
        this.rederij = rederij;
        this.containers = containers;
    }

    public Edge(Rederij rederij, List<Schip> schips) {
        this.rederij = rederij;
        this.schips = schips;
    }

    public Edge(Container container, Schip schip) {
        this.container = container;
        this.schip = schip;
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

    public Rederij getRederij() {
        return rederij;
    }

    public void setRederij(Rederij rederij) {
        this.rederij = rederij;
    }

    public List<Schip> getSchips() {
        return schips;
    }

    public void setSchips(List<Schip> schips) {
        this.schips = schips;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }
}
