package com.example.aijavagrammar;

import com.example.aijavagrammar.service.AiService;
import com.example.aijavagrammar.service.AudioService;
import com.example.aijavagrammar.service.InputModeService;
import com.example.aijavagrammar.service.TextInputService;

public class App {
    public static void main(String[] args) {
        AiService aiService = new AiService();
        AudioService audioService = new AudioService();
        TextInputService textInputService = new TextInputService();

        InputModeService inputModeService = new InputModeService(aiService, audioService, textInputService);
        inputModeService.textMode();
    }
}        
         
