package com.example.edge.model;

public class Rederij {
    private String id;

    private String naam;
    private String mail;
    private String telefoon;
    private String postcode;
    private String gemeente;

    public Rederij(String naam, String mail, String telefoon, String postcode, String gemeente) {
        this.naam = naam;
        this.mail = mail;
        this.telefoon = telefoon;
        this.postcode = postcode;
        this.gemeente = gemeente;
    }

    public Rederij() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelefoon() {
        return telefoon;
    }

    public void setTelefoon(String telefoon) {
        this.telefoon = telefoon;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getGemeente() {
        return gemeente;
    }

    public void setGemeente(String gemeente) {
        this.gemeente = gemeente;
    }
}
