package com.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Dossier {
    @JsonProperty("ref_dossier")
    private String refDossier;

    @JsonProperty("date_dossier")
    private String dateDossier;

    @JsonProperty("date_soin")
    private String dateSoin;

    @JsonProperty("ref_individu")
    private String refIndividu;

    @JsonProperty("ref_etablissement")
    private String refEtablissement;

    @JsonProperty("suivi_dossier")
    private String suiviDossier;

    @JsonProperty("lettre_cle")
    private String lettreCle;

    @JsonProperty("coeff")
    private int coeff;

    @JsonProperty("ref_acte")
    private String refActe;

    @JsonProperty("ref_avoir")
    private String refAvoir;

    @JsonProperty("ref_prat")
    private String refPrat;

    @JsonProperty("ccam")
    private String ccam;

    @JsonProperty("mt_facture")
    private double mtFacture;

    @JsonProperty("code_association")
    private String codeAssociation;

    @JsonProperty("is_optha")
    private String isOphta;

    // Getters et setters
    public String getRefDossier() {
        return refDossier;
    }

    public void setRefDossier(String refDossier) {
        this.refDossier = refDossier;
    }

    public String getDateDossier() {
        return dateDossier;
    }

    public void setDateDossier(String dateDossier) {
        this.dateDossier = dateDossier;
    }

    public String getDateSoin() {
        return dateSoin;
    }

    public void setDateSoin(String dateSoin) {
        this.dateSoin = dateSoin;
    }

    public String getRefIndividu() {
        return refIndividu;
    }

    public void setRefIndividu(String refIndividu) {
        this.refIndividu = refIndividu;
    }

    public String getRefEtablissement() {
        return refEtablissement;
    }

    public void setRefEtablissement(String refEtablissement) {
        this.refEtablissement = refEtablissement;
    }

    public String getSuiviDossier() {
        return suiviDossier;
    }

    public void setSuiviDossier(String suiviDossier) {
        this.suiviDossier = suiviDossier;
    }

    public String getLettreCle() {
        return lettreCle;
    }

    public void setLettreCle(String lettreCle) {
        this.lettreCle = lettreCle;
    }

    public int getCoeff() {
        return coeff;
    }

    public void setCoeff(int coeff) {
        this.coeff = coeff;
    }

    public String getRefActe() {
        return refActe;
    }

    public void setRefActe(String refActe) {
        this.refActe = refActe;
    }

    public String getRefAvoir() {
        return refAvoir;
    }

    public void setRefAvoir(String refAvoir) {
        this.refAvoir = refAvoir;
    }

    public String getRefPrat() {
        return refPrat;
    }

    public void setRefPrat(String refPrat) {
        this.refPrat = refPrat;
    }

    public String getCcam() {
        return ccam;
    }

    public void setCcam(String ccam) {
        this.ccam = ccam;
    }

    public double getMtFacture() {
        return mtFacture;
    }

    public void setMtFacture(double mtFacture) {
        this.mtFacture = mtFacture;
    }

    public String getCodeAssociation() {
        return codeAssociation;
    }

    public void setCodeAssociation(String codeAssociation) {
        this.codeAssociation = codeAssociation;
    }

    public String getIsOphta() {
        return isOphta;
    }

    public void setIsOphta(String isOphta) {
        this.isOphta = isOphta;
    }
}
