package com.example.aijavagrammar.service;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.TargetDataLine;

import org.vosk.Model;
import org.vosk.Recognizer;

import com.example.aijavagrammar.dto.AppConfigDTO;

public class RecognizerService {
    private Model voskModel;
    private Recognizer recognizer;
    private static final int BUFFER_SIZE = 8192;
    private static final int SAMPLE_RATE = 16000;
    private final byte[] buffer = new byte[BUFFER_SIZE];
    private static final Logger LOGGER = Logger.getLogger(RecognizerService.class.getName());
    private static final String MODEL_PATH = AppConfigDTO.get("MODEL_PATH");
    

    public void stop() {
        if(voskModel != null && recognizer != null) {
            try {
                if (recognizer.acceptWaveForm(new byte[0], 0)) {
                    recognizer.getFinalResult();
                } else {
                    recognizer.getPartialResult();
                }
                Thread.sleep(500);

                recognizer.close();
                Thread.sleep(200);
                voskModel.close();
                recognizer = null;
                voskModel = null;
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error while stopping Vosk", e);
            }
        }
    }

    public void init() {
        try {
            if(voskModel == null) {
                voskModel = new Model(MODEL_PATH);
            }
            if(recognizer == null) {
                recognizer = new Recognizer(voskModel, SAMPLE_RATE);        
            }
            System.out.println("Listening for speech... (Don't forget your sentence must starts with the word \"grammar\")");   
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load model or recognizer", e);
        }
    }

    public void recognizeAudioStream(TargetDataLine microphone, int minEnergyAvgLevel, Consumer<String> callback) {
        if (!microphone.isOpen() || recognizer == null) {
            return ;
        }
        int bytesRead = microphone.read(buffer, 0, buffer.length);
        if (bytesRead > 0 && isSpeechDetected(buffer, bytesRead, minEnergyAvgLevel) && recognizer.acceptWaveForm(buffer, bytesRead)) {
            String resultJson = recognizer.getResult();
            callback.accept(resultJson);
        }
    }
    
    private boolean isSpeechDetected(byte[] buffer, int bytesRead, int minEnergyAvgLevel) {
        double sum = 0;
        for (int i = 0; i < bytesRead; i++) {
            sum += buffer[i] * buffer[i];
        }
        double averageEnergy = sum / bytesRead;
        // System.out.println("AVG VOLUME ENERGY IS: "  + averageEnergy);
        return averageEnergy > minEnergyAvgLevel; 
    }
        
}
