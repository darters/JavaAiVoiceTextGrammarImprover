package com.example.aijavagrammar.handler;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.aijavagrammar.dto.AiResponseInfoDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.models.chat.completions.ChatCompletion;

public class AiResponseHandler {
    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static Logger LOGGER = Logger.getLogger(AiResponseHandler.class.getName());
    private final static Pattern pattern = Pattern.compile("1\\)(.*?)2\\)(.*?)$", Pattern.DOTALL);

    public void displayResponse(ChatCompletion response) {
        AiResponseInfoDTO aiResponseInfoDTO = new AiResponseInfoDTO();
        aiResponseInfoDTO = getResponseInfo(response);
        if(aiResponseInfoDTO != null) {
            System.out.println("✅ " + aiResponseInfoDTO.getCorrectResponse());
            System.out.println("🔊 " + aiResponseInfoDTO.getNaturalnessResponse());
            System.out.println(aiResponseInfoDTO.getTotalTokens());
        }
    }

    private AiResponseInfoDTO getResponseInfo(ChatCompletion response) {
        AiResponseInfoDTO aiResponseInfoDTO = new AiResponseInfoDTO();
        try {
            String responseTree = objectMapper.writeValueAsString(response);
            JsonNode rootNode = objectMapper.readTree(responseTree);
            String text = rootNode.path("choices").get(0).path("message").path("content").asText();
            Integer totalTokens = rootNode.path("usage").path("total_tokens").asInt();
            Matcher matcher = pattern.matcher(text);
            if(matcher.find()) {
                String correctResponse = matcher.group(1);
                String naturalnessResponse = matcher.group(2);
                aiResponseInfoDTO.setCorrectResponse(correctResponse);
                aiResponseInfoDTO.setNaturalnessResponse(naturalnessResponse);
                aiResponseInfoDTO.setTotalTokens(totalTokens);
            } else {
                LOGGER.log(Level.SEVERE, "Response text format unexpected: " + text);
                System.out.println("Response text format unexpected: " + text);
            }
            return aiResponseInfoDTO;
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Error occurred while parsing the response: " + exception);
            return null;
        }
    }
}
