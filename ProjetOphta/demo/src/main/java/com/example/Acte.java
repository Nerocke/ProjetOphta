package com.example;

public class Acte {
    private String code;
    private String description;
    private double prix;

    public Acte(String code, String description, double prix) {
        this.code = code;
        this.description = description;
        this.prix = prix;
    }

    // Getters et Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "Acte{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                '}';
    }
}
