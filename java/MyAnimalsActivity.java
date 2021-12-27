package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;

public class MyAnimalsActivity extends AppCompatActivity {
    private Set<String> animalList;
    private TextView tv;
    private String myAnimals = "";
    private final String PREFS_KEY = "highScore";
    private final String ANIMAL_SET_KEY = "Animal Set";

    private Set<String> loadCollectionStrings(){
        Set<String> setS = new HashSet<String>();
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        Set<String> ss = scorePrefs.getStringSet(ANIMAL_SET_KEY, setS);
        return ss;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_animals);

        tv = findViewById(R.id.collectedAnimalsList);
        animalList = loadCollectionStrings();

        Iterator<String> it = animalList.iterator();

        while(it.hasNext()){
            String s = it.next();
            myAnimals = myAnimals+s+"\n";
        }
        tv.setGravity(Gravity.CENTER);
        tv.setText(myAnimals);
        tv.setMovementMethod(new ScrollingMovementMethod());

    }
}
