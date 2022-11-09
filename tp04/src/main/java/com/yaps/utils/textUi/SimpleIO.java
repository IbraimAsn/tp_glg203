package com.yaps.utils.textUi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Simplified I/O interface.
 */

public class SimpleIO {
    private BufferedReader in;

    public SimpleIO() {
        this.in = new BufferedReader(new InputStreamReader(System.in));
    }

    public void printf(String format, Object... args) {
        System.out.printf(format, args);
    }

    public void flush() {
        System.out.flush();
    }

    public String readLine() {
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Display a message on a line
     * 
     * @param message
     */
    public void showMessage(String message) {
        printf("%s\n", message);
    }

    /**
     * Asks for a given answer (which can be empty)
     * 
     * @param message : message to display
     * @return
     */
    public String askForString(String message) {
        printf(message+ ": ");
        flush();
        return readLine().trim();
    }

    public void waitEnter() {
        showMessage("\n\tPress enter to continue...");
        readLine();
    }

    public void println(String welcomeText) {
        System.out.println(welcomeText);
    }

    public void println() {
        System.out.println();
    }

    /**
     * Ask for an int between two values (inclusives)
     * @param message message to display
     * @param min minimal value
     * @param max maximal value
     * @return the chosen value.
     */
    public int askForInt(String message, int min, int max) {
        boolean answerNotFound = true;
        // dummy init
        // (java can't figure out that if answerNotFound is false, answer has a value)
        int answer=0;
        do {
            println(message);
            printf("please enter a value between %s and %s: ", min, max);
            String answerString = readLine();
            try {
                answer = Integer.parseInt(answerString);
                if (min <= answer && answer <= max) {
                    answerNotFound = false;
                }
            } catch (NumberFormatException e) {
                println("Please enter an integer");
            }           
        } while (answerNotFound);
        return answer;
    }

    public double askForPositiveDouble(String message) {
        Double result = null;// dirty trick...
        do {
            final String errorMessage = "please enter a positive number";
            String string = askForString(message);
            try {
                result = Double.valueOf(string);
            } catch (NumberFormatException exception) {
                println(errorMessage);
            }
            if (result < 0.0) {
                println(errorMessage);
            }
        } while (result == null || result < 0.0);
        return result;
    }

}
