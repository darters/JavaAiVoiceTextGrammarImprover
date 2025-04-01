package com.example.aijavagrammar.service;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.vosk.Model;
import org.vosk.Recognizer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AudioService {
    private static final int SAMPLE_RATE = 16000;
    private static final int BUFFER_SIZE = 8000;
    private static final Logger LOGGER = Logger.getLogger(AudioService.class.getName());
    private static final String MODEL_PATH = "/home/yehor/models/vosk-model-en-us-0.42-gigaspeech";
    private TargetDataLine microphone;
    private ObjectMapper objectMapper;


    public void startListening(Consumer<String> callback) {
        try {
            Model voskModel = new Model(MODEL_PATH);
            Recognizer recognizer = new Recognizer(voskModel, SAMPLE_RATE);
            microphone = configureMicrophone();
            microphone.start();

            System.out.println("Listening for speech... (Don't forget your sentence must starts with the word \"grammar\")"); 
            while(true) {
                String recognizedText = processAudioStream(microphone, recognizer);
                if(recognizedText != null) {
                    String text = extractResult(recognizedText);
                    if(text != null) {
                        callback.accept(text);
                        System.out.println("Text is: " + text);
                    }    
                }   
            } 
        } catch (IOException | LineUnavailableException exception) {
            LOGGER.log(Level.SEVERE, "Error while accessing the microphone line. It might be in use by another app", exception);;
        }
    }
    public void stopListening() {
        microphone.stop();
        microphone.close();
    }
    private boolean isSpeechDetected(byte[] buffer, int bytesRead) {
        double sum = 0;
        for (int i = 0; i < bytesRead; i++) {
            sum += buffer[i] * buffer[i];
        }
        double averageEnergy = sum / bytesRead;
        return averageEnergy > 500; 
    }
        
    private String processAudioStream(TargetDataLine microphone, Recognizer recognizer) {
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = microphone.read(buffer, 0, buffer.length);
        try {
            if (bytesRead > 0 && isSpeechDetected(buffer, bytesRead) && recognizer.acceptWaveForm(buffer, bytesRead)) {
                String resultJson = recognizer.getResult();
                return resultJson;
            }
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Error while processing audio", exception);
        }
        return null;
    }

    private String extractResult(String resultJson) {
        String text = null;
        objectMapper = new ObjectMapper();

        if (resultJson.contains("grammar")) {
            try {
                JsonNode jsonNode = objectMapper.readTree(resultJson);
                text = jsonNode.get("text").asText().replace("grammar", "");
            } catch (Exception exception) {
                LOGGER.log(Level.SEVERE, "Error while extracting data from response", exception);
            }
        }
        return text;
    }
    private TargetDataLine configureMicrophone() throws LineUnavailableException {
        AudioFormat audioFormat = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
        TargetDataLine targetDataLine = AudioSystem.getTargetDataLine(audioFormat);   
        targetDataLine.open(audioFormat); 
        return targetDataLine;
    }
}
