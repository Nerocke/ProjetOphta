package com.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;


public class Facturations {
    public static void VerifPaiement(List<Ophta> ophtaRecords, List<Dossier> dossiers) {
        Map<String, List<Dossier>> dossierMap = new HashMap<>();
        for (Dossier dossier : dossiers) {
            dossierMap.computeIfAbsent(dossier.getRefDossier(), k -> new ArrayList<>()).add(dossier);
        }

        // Vérification
        for (Ophta record : ophtaRecords) {
    
            boolean hasOCT = record.getRubrique().contains("oct");
            boolean hasPACHY = record.getRubrique().contains("pachy");

            if (hasOCT && hasPACHY) {
                System.out.println("Erreur : OCT et PACHY facturés ensemble pour IPP " + record.getIpp() + " le " + record.getHeure());
            }

        }
    }
}
