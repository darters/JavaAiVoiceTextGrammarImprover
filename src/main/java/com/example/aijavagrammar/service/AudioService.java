package com.example.aijavagrammar.service;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class AudioService {
    private static final int SAMPLE_RATE = 16000;
    private static final Logger LOGGER = Logger.getLogger(AudioService.class.getName());
    private TargetDataLine microphone;
    private boolean listening = false;

    public void startListening(Consumer<TargetDataLine> callback) {
        configureMicrophone();
        if(!listening) {
            listening = true;
            microphone.start();
            
            while (listening) {
                callback.accept(microphone);   
            }    
        }

    }
    public void stopListening() {
        listening = false;
        if(microphone != null) {
            microphone.close();
        }
    }

    private void configureMicrophone() {
        try {
            AudioFormat audioFormat = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
            microphone = AudioSystem.getTargetDataLine(audioFormat);   
            microphone.open(audioFormat); 
        } catch (LineUnavailableException exception) {
            LOGGER.log(Level.SEVERE, "Error while accessing the microphone line. It might be in use by another app", exception);
            microphone = null;
        }
    }

}
