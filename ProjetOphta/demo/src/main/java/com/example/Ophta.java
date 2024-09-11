package com.example;

public class Ophta {
    private int id;
    private String ipp;
    private String heure;
    private String rubrique;
    private String donnee;



    // Getters and Setters


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getIpp() {
        return ipp;
    }

    public String getDonee() {
        return donnee;
    }

    public void setDonnee(String donnee) {
        this.donnee = donnee;
    }




    public void setIpp(String ipp) {
        this.ipp = ipp;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String date) {
        this.heure= date;
    }

    public String getRubrique() {
        return rubrique;
    }

    public void setRubrique(String rubrique) {
        this.rubrique = rubrique;
    }


    public boolean isValid() {
        // check pour voir si les colonnes ne sont pas null
        return (id != -1) && ipp != null && !ipp.trim().isEmpty() &&
               heure != null && !heure.trim().isEmpty() &&
               rubrique != null && !rubrique.trim().isEmpty() &&
               donnee != null && !donnee.trim().isEmpty();
    }
}
