package com.example.myapplication;

public class TrueFalse {
    boolean Veracity;
    String Question;

    public TrueFalse(String question, boolean veracity){
        Question = question;
        Veracity = veracity;
    }

    public String getQuestion() {
        return Question;
    }

    public boolean getVeracity() {
        return Veracity;
    }

    public boolean checkAnswer(boolean answer){
        boolean correct = false;
        if (answer == Veracity){
            correct = true;
        }
        return correct;
    }
}
