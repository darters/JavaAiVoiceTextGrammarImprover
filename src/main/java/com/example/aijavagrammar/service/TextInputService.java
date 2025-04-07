package com.example.aijavagrammar.service;

import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TextInputService {
    private static final Logger LOGGER = Logger.getLogger(TextInputService.class.getName());
    private final Scanner scanner = new Scanner(System.in); 


    public void getText(Consumer<String> callback) {
        while (true) {
            String text = scanner.nextLine();
            callback.accept(text);
        }
    }
    
    public boolean validateSentence(String sentence, int minLength, int maxLength) {
        if (sentence == null) { 
            return false;
        }

        int checkingForSentence = sentence.replace(" ", "").length();
        if(checkingForSentence < minLength || checkingForSentence > maxLength) {
            LOGGER.log(Level.SEVERE, "Error, your sentence must be more than " + (minLength - 1) + " and less than " + maxLength);
            return false;
        }
        return true;
    }

}
