package com.example.myapplication;
import java.io.Serializable;

public class Animal implements Serializable{
    private String Name;
    private String Description;
    private String myDrawable;
    private String Habitat;
    private int ID;

    public Animal(String name, String desc, String drawableName) {
        Name = name;
        Description = desc;
        myDrawable = drawableName;
    }
    public Animal(String name, String desc, String drawableName, int ID) {
        Name = name;
        Description = desc;
        myDrawable = drawableName;
    }


    public String getName() {
        return this.Name;
    }

    public String getDesc() {
        return this.Description;
    }

    public String getIM() {
        return this.myDrawable;
    }

    public int getID(){ return this.ID;}
}
