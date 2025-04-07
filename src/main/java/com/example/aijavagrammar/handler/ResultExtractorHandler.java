package com.example.aijavagrammar.handler;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResultExtractorHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper(); 
    private static final Logger LOGGER = Logger.getLogger(ResultExtractorHandler.class.getName());

    public void extractResult(String resultJson, Consumer<String> callback) {
        if (resultJson.toLowerCase().contains("grammar")) {
            try {
                JsonNode jsonNode = objectMapper.readTree(resultJson);
                String text = jsonNode.get("text").asText().replace("grammar", "");
                callback.accept(text);
            } catch (Exception exception) {
                LOGGER.log(Level.SEVERE, "Error while extracting data from response: ", exception);
            }
        }
    }
}
