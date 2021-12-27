package com.example.myapplication;

import java.util.ArrayList;
import java.util.Random;

class QuestionPack {
    private String imagePath;
    private Animal option1;
    private Animal option2;
    private Animal option3;
    private Animal option4;
    private String answer;

    public QuestionPack(Animal op1, Animal op2, Animal op3, Animal op4){
        option1 = op1;
        option2 = op2;
        option3 = op3;
        option4 = op4;
        setAnswerAndImage();
    }

    public void setAnswerAndImage(){
        Random r = new Random();
        int picked = r.nextInt(3);
        ArrayList<Animal> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);
        options.add(option3);
        options.add(option4);
        answer = (options.get(picked).getName());
        imagePath = (options.get(picked).getIM());
    }
    public String getImagePath() {
        return imagePath;
    }

    public String getOption1() {
        return option1.getName();
    }

    public String getOption2() {
        return option2.getName();
    }

    public String getOption3() {
        return option3.getName();
    }

    public String getOption4() {
        return option4.getName();
    }

    public String getAnswer() {
        return answer;
    }
}
