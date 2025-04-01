package com.example.aijavagrammar.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.aijavagrammar.dto.AiResponseInfoDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

import io.github.cdimascio.dotenv.Dotenv;

public class AiService {
    private Dotenv dotenv = Dotenv.load();
    private ObjectMapper objectMapper = new ObjectMapper();
    private String apiKey = dotenv.get("OPENAI_API_KEY"); 
    private String prompt = "You correct grammar and improve sentence naturalness Reply with two versions:1)Just the grammatically corrected version 2)A more natural native-like version";
    private OpenAIClient client = OpenAIOkHttpClient.builder()
        .apiKey(apiKey)
        .build();

    public ChatCompletion createRequestAi(String request) {
         ChatCompletionCreateParams createaParamsBuilder = ChatCompletionCreateParams.builder()
            .model(ChatModel.GPT_3_5_TURBO)
            .maxCompletionTokens(200)
            .addDeveloperMessage(prompt)
            .addUserMessage(request)
            .temperature(0.7)
            .build();

        ChatCompletion response = client.chat().completions().create(createaParamsBuilder);
        return response;
    }
    public AiResponseInfoDTO getResponseInfo(ChatCompletion response) {
        AiResponseInfoDTO aiResponseInfoDTO = null;
        try {
            String responseTree = objectMapper.writeValueAsString(response);
            JsonNode rootNode = objectMapper.readTree(responseTree);
            String text = rootNode.path("choices").get(0).path("message").path("content").asText();
            Integer totalTokens = rootNode.path("usage").path("total_tokens").asInt();
            aiResponseInfoDTO = new AiResponseInfoDTO();
            Pattern pattern = Pattern.compile("1\\)(.*?)2\\)(.*?)$", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(text);
            if(matcher.find()) {
                String correctResponse = matcher.group(1);
                String naturalnessResponse = matcher.group(2);
                aiResponseInfoDTO.setCorrectResponse(correctResponse);
                aiResponseInfoDTO.setNaturalnessResponse(naturalnessResponse);
                aiResponseInfoDTO.setTotalTokens(totalTokens);
            } else {
                System.out.println("Something went wrong while parsing GPT response: " + text);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return aiResponseInfoDTO;
    }
}
