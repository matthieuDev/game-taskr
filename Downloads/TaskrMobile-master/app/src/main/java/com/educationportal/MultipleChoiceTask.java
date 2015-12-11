package com.educationportal;

/**
 * Created by johnearle on 9/27/15.
 */
public class MultipleChoiceTask extends Task {
    String[] options;
    int answer;

    public MultipleChoiceTask(String question, String[] options, int answer) {
        this.options = options;
        this.answer = answer;
        this.question = question;
    }
}
