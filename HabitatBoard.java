package com.example.myapplication;

import android.graphics.Color;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HabitatBoard {
    Habitat answer, Habitat1, Habitat2, Habitat3;
    int habitatBackground1, habitatBackground2, habitatBackground3, habitatBackgroundAnswer;
    String habitatName1, habitatName2, habitatName3, habitatNameAnswer, animalName;
    TextView One, Two, Three, Four;

    private TextView[] shuffleDropZones(TextView[] habitatDropZones){
        List<TextView> dzList = Arrays.asList(habitatDropZones);
        Collections.shuffle(dzList);
        dzList.toArray(habitatDropZones);
        return habitatDropZones;
    }

    private void setTextViews(TextView[] textSet){
        One = textSet[0];
        Two = textSet[1];
        Three = textSet[2];
        Four = textSet[3];
    }

    private void setHabitats(Habitat[] habitatSet){
        answer = habitatSet[0];
        Habitat1 = habitatSet[1];
        Habitat2 = habitatSet[2];
        Habitat3 = habitatSet[3];
    }

    private void setHabitatBackgrounds(){
        habitatBackground1 = Color.argb(255, Habitat1.getR(), Habitat1.getG(), Habitat1.getB());
        habitatBackground2 = Color.argb(255, Habitat2.getR(), Habitat2.getG(), Habitat2.getB());
        habitatBackground3 = Color.argb(255, Habitat3.getR(), Habitat3.getG(), Habitat3.getB());
        habitatBackgroundAnswer = Color.argb(255, answer.getR(), answer.getG(), answer.getB());
    }

    private void setHabitatNames(){
        habitatName1 = Habitat1.getHabitat();
        habitatName2 = Habitat2.getHabitat();
        habitatName3 = Habitat3.getHabitat();
        habitatNameAnswer = answer.getHabitat();
    }

    public HabitatBoard(Habitat h1, Habitat h2, Habitat h3, Habitat h4, TextView one, TextView two, TextView three, TextView four){
        System.out.println("In Habitat Board Constructor");
        Habitat[] habitatSet = {h1, h2, h3, h4};
        TextView[] textSet = {one, two, three, four};
        textSet = shuffleDropZones(textSet);
        setTextViews(textSet);
        setHabitats(habitatSet);
        setHabitatBackgrounds();
        setHabitatNames();
        animalName = answer.getAnimal();
    }

    public int getHabitatBackground1() {
        return habitatBackground1;
    }

    public int getHabitatBackground2() {
        return habitatBackground2;
    }

    public int getHabitatBackground3() {
        return habitatBackground3;
    }

    public int getHabitatBackgroundAnswer() {
        return habitatBackgroundAnswer;
    }

    public String getAnimalName() {
        return animalName;
    }

    public String getHabitatName1() {
        return habitatName1;
    }

    public String getHabitatName2() {
        return habitatName2;
    }

    public String getHabitatName3() {
        return habitatName3;
    }

    public String getHabitatNameAnswer() {
        return habitatNameAnswer;
    }

    public TextView getOne() {
        return One;
    }

    public TextView getTwo() {
        return Two;
    }

    public TextView getThree() {
        return Three;
    }

    public TextView getFour() {
        return Four;
    }
}
