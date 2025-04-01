package com.example.aijavagrammar.service;

import com.example.aijavagrammar.dto.AiResponseInfoDTO;
import com.openai.models.chat.completions.ChatCompletion;

public class InputModeService {
    private final AiService aiService;
    private final AudioService audioService;
    private final TextInputService textInputService;
    private ChatCompletion chatCompletion;
    private AiResponseInfoDTO aiResponseInfoDTO = new AiResponseInfoDTO();
    public InputModeService(AiService aiService, AudioService audioService, TextInputService textInputService) {
        this.aiService = aiService;
        this.audioService = audioService;
        this.textInputService = textInputService;
    }

    
    public void textMode() { 
        textInputService.getText(text -> {
            processSentence(text, "Awaiting text");
        });
    }
    public void voiceMode() {
        audioService.startListening(text -> {
            processSentence(text, "Awaiting voice");
        });
    }

    private void processSentence(String text, String waitingMessage) {
        if(textInputService.validateSentence(text, 3, 1000)) {
            createRequestShowResponse(text);
        }
        System.out.println(waitingMessage + " ..."); 
    }
    private void createRequestShowResponse(String text) {
        chatCompletion = aiService.createRequestAi(text);
        aiResponseInfoDTO = aiService.getResponseInfo(chatCompletion);
        System.out.println("✅ " + aiResponseInfoDTO.getCorrectResponse());
        System.out.println("🔊 " + aiResponseInfoDTO.getNaturalnessResponse());
        System.out.println(aiResponseInfoDTO.getTotalTokens());
    }
}
