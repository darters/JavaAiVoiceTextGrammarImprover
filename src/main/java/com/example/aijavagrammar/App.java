package com.example.aijavagrammar;

import com.example.aijavagrammar.handler.ResultExtractorHandler;
import com.example.aijavagrammar.processor.InputProcessor;
import com.example.aijavagrammar.service.AudioService;
import com.example.aijavagrammar.service.ModeService;
import com.example.aijavagrammar.service.RecognizerService;
import com.example.aijavagrammar.service.TextInputService;

public class App {
    public static void main(String[] args) {
        AudioService audioService = new AudioService();
        TextInputService textInputService = new TextInputService();
        RecognizerService recognizerService = new RecognizerService();
        ResultExtractorHandler resultExtractorHandler = new ResultExtractorHandler();

        ModeService modeService = new ModeService(audioService, textInputService, resultExtractorHandler, recognizerService);
        InputProcessor inputProcessor = new InputProcessor(modeService);
        inputProcessor.start();
    }
}        
         
