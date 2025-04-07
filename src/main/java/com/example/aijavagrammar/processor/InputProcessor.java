package com.example.aijavagrammar.processor;

import com.example.aijavagrammar.handler.AiResponseHandler;
import com.example.aijavagrammar.model.InputMode;
import com.example.aijavagrammar.service.AiService;
import com.example.aijavagrammar.service.ModeService;
import com.example.aijavagrammar.service.TextInputService;
import com.openai.models.chat.completions.ChatCompletion;

public class InputProcessor {
    private final ModeService modeService;
    private final AiService aiService = new AiService();
    private final TextInputService textInputService = new TextInputService();
    private final AiResponseHandler aiResponseHandler = new AiResponseHandler();

    public InputProcessor(ModeService modeService) {
        this.modeService = modeService;
    }
    
    public void start() {
        while (true) {
            modeService.textMode(text -> {
                InputMode currentMode = modeService.changeMode(text);
                if(currentMode == InputMode.VOICE) {
                    modeService.voiceMode(recognizedText -> {                      
                        processSentence(recognizedText, "Awaiting speech");
                    });
                }
                if (currentMode == null) {
                    stopProgram(text, "stop");
                    processSentence(text, "Awaiting text");
                }
    
            });
        }       
    }
    private void stopProgram(String text, String command) {
        if (text.toLowerCase().equals(command)) {
            System.exit(0);   
        }
    }

    private void processSentence(String text, String waitingMessage) {
        if(textInputService.validateSentence(text, 3, 1000)) {
            ChatCompletion response = aiService.createRequestAi(text);
            aiResponseHandler.displayResponse(response);
        }
        System.out.println(waitingMessage + " ..."); 
    }
}
