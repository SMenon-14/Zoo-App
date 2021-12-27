package com.example.myapplication;

public class Habitat {
    String Animal, Habitat;
    int R, G, B;
    public Habitat(String animal, String habitat, int red, int green, int blue){
        Animal = animal;
        Habitat = habitat;
        R = red;
        G = green;
        B = blue;
    }

    public String getAnimal() { return Animal; }

    public String getHabitat() {
        return Habitat;
    }

    public int getR() {
        return R;
    }

    public int getG() {
        return G;
    }

    public int getB() {
        return B;
    }
}
