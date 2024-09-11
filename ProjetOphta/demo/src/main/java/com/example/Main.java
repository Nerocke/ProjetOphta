package com.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("output.txt", true))) {
            List<Ophta> ophtaRecords = CsvReader.readOphtaRecords("demo\\src\\main\\java\\com\\example\\resources\\ophta.csv");
            List<Dossier> dossiers = JsonReader.readDossiers("demo\\src\\main\\java\\com\\example\\resources\\dossiers.json");

            // Mapping des actes déjà facturés
            Map<String, Map<String, Double>> actesPayes = processDossiers(dossiers);

            // Vérification et création des factures
            VerificationGenerationFactures(ophtaRecords, actesPayes, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour traiter les dossiers et obtenir les actes déjà facturés
    private static Map<String, Map<String, Double>> processDossiers(List<Dossier> dossiers) {
        Map<String, Map<String, Double>> actesPayes = new HashMap<>();
        for (Dossier dossier : dossiers) {
            String refDossier = dossier.getRefDossier();
            String ccam = dossier.getCcam(); 

            if (ccam != null && !ccam.trim().isEmpty()) {
                double valeurCCAM = getCCAMPrice(ccam, false); 
                actesPayes.computeIfAbsent(refDossier, k -> new HashMap<>())
                          .put(ccam, valeurCCAM);
            }
        }
        return actesPayes;
    }

    // Vérification des paiements et génération des factures
    private static void VerificationGenerationFactures(List<Ophta> ophtaRecords, Map<String, Map<String, Double>> actesPayes, PrintWriter writer) {
        Map<String, Map<String, Double>> actesParDateEtIpp = new LinkedHashMap<>();
        
        for (Ophta record : ophtaRecords) {
            String ipp = record.getIpp();
            String dateHeure = record.getHeure();
        
            if (dateHeure == null || !dateHeure.contains(" ")) continue;

            String date = dateHeure.split(" ")[0];
            String rubrique = record.getRubrique().toLowerCase();
            String key = ipp + "_" + date; 
            String ccam = getCcamForActe(rubrique);

            if (!ccam.isEmpty()) {
                actesParDateEtIpp.computeIfAbsent(key, k -> new HashMap<>())
                    .merge(ccam, getCCAMPrice(ccam, false), Double::sum);
            }
        }
        
        for (Map.Entry<String, Map<String, Double>> entry : actesParDateEtIpp.entrySet()) {
            String key = entry.getKey(); // clé IPP et date
            Map<String, Double> actes = entry.getValue();

            List<Map.Entry<String, Double>> actesTrie = actes.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .collect(Collectors.toList());

            boolean hasConsultation = actesTrie.stream().anyMatch(e -> Arrays.asList("C", "CS", "CSU", "APC", "CAT").contains(e.getKey()));
            boolean hasOCT = actesTrie.stream().anyMatch(e -> e.getKey().equals("BZQK001"));
            boolean hasPACHY = actesTrie.stream().anyMatch(e -> e.getKey().equals("BDQP003"));

            boolean canFacturerRetino = hasOCT || actesTrie.stream().anyMatch(e -> e.getKey().equals("BGQP007"));

            if (hasOCT && hasPACHY) {
                actesTrie.removeIf(e -> e.getKey().equals("BZQK001") || e.getKey().equals("BDQP003"));
            }

            double totalDeuxActes = 0.0;
            int acteFactures = 0;

            for (int i = 0; i < actesTrie.size() && acteFactures < 2; i++) {
                String acteCode = actesTrie.get(i).getKey();
                if (!Arrays.asList("C", "CS", "CSU", "APC", "CAT").contains(acteCode)) {
                    double montantActe = getCCAMPrice(acteCode, acteFactures == 1); 
                    totalDeuxActes += montantActe;
                    acteFactures++;
                }
            }

            if (totalDeuxActes < 30.0 && hasConsultation) {
                double montantConsultation = getCCAMPrice("C", false); 
                writer.println("Consultation facturée pour IPP " + key.split("_")[0] + " le " + key.split("_")[1]);
                writer.println("Montant attendu: " + montantConsultation);
                writer.println();
            } else {
                traitemenFactures(actesTrie, writer, key, totalDeuxActes, acteFactures, hasConsultation, canFacturerRetino);
            }
        }
    }

    // Méthode pour traiter les factures
    private static void traitemenFactures(List<Map.Entry<String, Double>> actesTrie, PrintWriter writer, String key, double totalDeuxActes, int acteFactures, boolean hasConsultation, boolean canFacturerRetino) {
        double total = 0.0;
        acteFactures = 0;

        for (int i = 0; i < actesTrie.size() && acteFactures < 2; i++) {
            String acteCode = actesTrie.get(i).getKey();
            double montantAttendu = getCCAMPrice(acteCode, acteFactures == 1); 
            String descriptionActe = getDescriptionForCcam(acteCode);


            if (Arrays.asList("C", "CS", "CSU", "APC", "CAT").contains(acteCode)) {
                if (totalDeuxActes < 30.0) {
                    writer.println("Consultation facturée pour IPP " + key.split("_")[0] + " le " + key.split("_")[1]);
                    total = montantAttendu;
                    break; 
                }
            } else {
                if (acteCode.equals("BGQP007") && !canFacturerRetino) continue; 


                writer.println("Acte " + descriptionActe + " facturé pour IPP " + key.split("_")[0] + " le " + key.split("_")[1]);
                total += montantAttendu;
                writer.println("Montant attendu: " + montantAttendu);
                acteFactures++;
            }
        }

        if (acteFactures == 0 && hasConsultation) {
            total = getCCAMPrice("C", false);
            writer.println("Consultation facturée seule pour IPP " + key.split("_")[0] + " le " + key.split("_")[1]);
        }

        writer.println("Total pour IPP " + key.split("_")[0] + " le " + key.split("_")[1] + ": " + total);
        writer.println(); 
    }

    // Méthode pour obtenir le prix CCAM
    private static double getCCAMPrice(String ccam, boolean isSecondActe) {
        double montant = getMontantCCAM(ccam); 
        if (isSecondActe) {
            montant *= 0.5; 
        }
        return montant;
    }

    private static String getCcamForActe(String acte) {
        switch (acte) {
            case "oct":
                return "BZQK001";
            case "ret":
                return "BGQP007";
            case "pachy":
                return "BDQP003";
            case "fo":
                return "BGQP002";
            case "cv":
                return "BLQP004";
            case "ord":
            case "o":
            case "laf":
            case "rf":
            case "vp":
            case "cat":
                return "C"; 
            case "orth":
                return "AMY";
            default:
                return "";
        }
    }

    private static double getMontantCCAM(String ccam) {
        switch (ccam) {
            case "BZQK001":
                return 50.0;
            case "BGQP007":
                return 20.0;
            case "BDQP003":
                return 19.0;
            case "BGQP002":
                return 28.0;
            case "BLQP004":
                return 33.0;
            case "C":
            case "CS":
            case "CSU":
            case "APC":
            case "CAT":
                return 30.0;
            case "AMY":
                return 35.0;
            default:
                return 0.0;
        }
    }






    private static String getDescriptionForCcam(String ccam) {
        switch (ccam) {
            case "BZQK001":
                return "OCT"; 
            case "BGQP007":
                return "RETINO"; 
            case "BDQP003":
                return "PACHY"; 
            case "BGQP002":
                return "FO"; 
            case "BLQP004":
                return "CV"; 
            case "C":
            case "CS":
            case "CSU":
            case "APC":
            case "CAT":
                return "Consultation";
            case "AMY":
                return "Orthoptie";
            default:
                return "Inconnu";
        }
    }
}
