package com.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CsvReader {

    // Lecture CSV
    public static List<Ophta> readOphtaRecords(String filePath) throws IOException {
        List<Ophta> records = new ArrayList<>();

        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader() 
                     .withDelimiter(';') 
                     .withIgnoreSurroundingSpaces() 
                     .withQuote('"'))) { 

            Map<String, Integer> headers = csvParser.getHeaderMap();

            if (!headers.containsKey("id") || !headers.containsKey("ipp") ||
                !headers.containsKey("heure") || !headers.containsKey("rubrique") ||
                !headers.containsKey("donnee")) {
            }

            for (CSVRecord csvRecord : csvParser) {
                Ophta record = new Ophta();

                try {


                    record.setId(parseInt(csvRecord.get("id"))); // ID
                    record.setIpp(csvRecord.get("ipp")); // IPP
                    record.setHeure(csvRecord.get("heure")); // Heure
                    record.setRubrique(csvRecord.get("rubrique")); // Rubrique
                    record.setDonnee(csvRecord.get("donnee")); // Donnée

                    // Ajout 
                    records.add(record);
                } catch (NumberFormatException e) {
                    // Gestion 
                    System.err.println("Erreur de format pour l'enregistrement : " + csvRecord);
                }
            }
        }

        return records;
    }

    // Test pour les erreurs (inutile parce que le programme est parfait)
    private static int parseInt(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            System.err.println("Erreur de conversion pour la valeur entière : " + value);
            return -1; 
        }
    }
}
