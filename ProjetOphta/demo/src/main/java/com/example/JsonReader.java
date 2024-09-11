package com.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonReader {
    public static List<Dossier> readDossiers(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // JSON -> JSONNODE
        JsonNode rootNode = mapper.readTree(new File(filePath));

        // check de la clé
        JsonNode resultsNode = rootNode.path("results");
        if (resultsNode.isMissingNode()) {
            throw new IllegalArgumentException("Le fichier JSON est mal formé ou ne contient pas la clé 'results'");
        }

        // liste items
        JsonNode itemsNode = resultsNode.path("items");
        if (itemsNode.isMissingNode() || !itemsNode.isArray()) {
            throw new IllegalArgumentException("Le fichier JSON est mal formé ou ne contient pas la clé 'items'");
        }

        // items -> liste de dossier
        List<Dossier> dossiers = new ArrayList<>();
        for (JsonNode itemNode : itemsNode) {
            Dossier dossier = mapper.treeToValue(itemNode, Dossier.class);
            dossiers.add(dossier);
        }

        return dossiers;
    }
}
