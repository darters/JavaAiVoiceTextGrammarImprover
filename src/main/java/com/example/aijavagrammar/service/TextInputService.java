package com.example.aijavagrammar.service;

import java.util.Scanner;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import com.example.aijavagrammar.model.InputMode;

public class TextInputService {
    private final Scanner scanner = new Scanner(System.in);
    private final Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");
    public InputMode currentMode = InputMode.TEXT; 


    public void getText(Consumer<String> callback) {
        System.out.println("Input your text...");
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
        if(checkingForSentence < minLength || checkingForSentence > maxLength && sentence != null) {
            System.out.println("Error, your sentence must be more than " + (minLength - 1) + " and less than " + maxLength);
        }
        return true;
    }
    private boolean changeMode(String text) {
        text = text.replaceAll(" ", "").toLowerCase();
        System.out.println("Text for checking " + text);
        if (text.equals("voice")) {
            System.out.println("Current mode: voice");
            currentMode = InputMode.VOICE;
            return true;
        } else if (text.equals("text")) {
            System.out.println("Current mode: text");
            currentMode = InputMode.TEXT;
            return true;
        }
        return false;
    }
}
