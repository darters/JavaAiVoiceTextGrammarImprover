package com.example.aijavagrammar.service;

import com.example.aijavagrammar.dto.AppConfigDTO;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
public class AiService {
    private final static String apiKey = AppConfigDTO.get("OPENAI_API_KEY");
    private final static String prompt = "You correct grammar and improve sentence naturalness Reply with two versions:1)Just the grammatically corrected version 2)A more natural native-like version";
    private final static OpenAIClient client = OpenAIOkHttpClient.builder()
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
    
}
