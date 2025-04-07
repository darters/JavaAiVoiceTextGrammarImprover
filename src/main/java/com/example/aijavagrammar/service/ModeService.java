package com.example.aijavagrammar.service;

import java.util.function.Consumer;

import com.example.aijavagrammar.handler.ResultExtractorHandler;
import com.example.aijavagrammar.model.InputMode;

public class ModeService {
    private final AudioService audioService;
    private final TextInputService textInputService;
    private final ResultExtractorHandler resultExtractorHandler;
    private final RecognizerService recognizerService;
    private InputMode currentMode = InputMode.TEXT; 

    private Thread voiceModeThread;


    public ModeService(AudioService audioService, TextInputService textInputService, 
    ResultExtractorHandler resultExtractorHandler, RecognizerService recognizerService) {
        this.audioService = audioService;
        this.textInputService = textInputService;
        this.resultExtractorHandler = resultExtractorHandler;
        this.recognizerService = recognizerService;
    }

    public void textMode(Consumer<String> callback) { 
        System.out.println("Input text, or change mode (voice, text)");
        textInputService.getText(text -> {
            callback.accept(text);
        });
    }
    public void voiceMode(Consumer<String> callback) {
        voiceModeThread = new Thread(() -> {
            recognizerService.init();
            audioService.startListening(audio -> {
                recognizerService.recognizeAudioStream(audio, 10, textJson -> {
                    resultExtractorHandler.extractResult(textJson, text -> {
                        System.out.println("Recognized text is: " + text);
                        callback.accept(text);
                    });
                }); 
        
            });
          
        });
        voiceModeThread.start();
    }

    public void stopVoiceMode() {
        audioService.stopListening();
        recognizerService.stop();
    }

    public InputMode changeMode(String text) {
        String cleanedText = text.toLowerCase().replace(" ", ""); 
        if(cleanedText.equals("voice")) {
            currentMode = InputMode.VOICE;
            return currentMode;
        } else if (cleanedText.equals("text")) {
            System.out.println("Input your text");
            currentMode = InputMode.TEXT;
            stopVoiceMode();
            return currentMode;
        }
        return null;
    }
 

}
