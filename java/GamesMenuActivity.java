package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class GamesMenuActivity extends AppCompatActivity {
    private Context context = this;
    private Button Quiz, Habitats, Matching, MyZoo;
    private HashMap<String, String> descripMap, imageMap;

    private void initializeAll(){
        Quiz = findViewById(R.id.button_to_quiz);
        Habitats = findViewById(R.id.button_to_habitats);
        Matching = findViewById(R.id.button_to_matching);
        MyZoo = findViewById(R.id.button_to_my_zoo);
    }

    public void readAnimalData() {
        descripMap = new HashMap<String, String>();
        imageMap = new HashMap<String, String>();

        InputStream is = getResources().openRawResource(R.raw.animal_info);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";

        try {
            reader.readLine();
            while ((line = reader.readLine()) != null) {

                String [] tokens = line.split(",");
                String aniName = tokens[0];
                String aniDesc = tokens[1];
                String imgName = tokens[2];
                imageMap.put(aniName, imgName);
                descripMap.put(aniName, aniDesc);
            }
        } catch (IOException e) {

            Log.wtf("MyActivity","Error reading data file on line "+line, e);
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.games_menu);
        initializeAll();
        Intent intent = getIntent();
        if (intent.hasExtra("AnimalImages") && intent.hasExtra("AnimalDescriptions")){
            imageMap = (HashMap<String, String>) intent.getSerializableExtra("AnimalImages");
            descripMap = (HashMap<String, String>) intent.getSerializableExtra("AnimalDescriptions");
        }
        else{
            readAnimalData();
        }
        Quiz.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GamesMenuActivity.this, QuizActivity.class);
                intent.putExtra("AnimalImages", imageMap);
                intent.putExtra("AnimalDescriptions", descripMap);
                // start the activity connect to the specified class
                startActivity(intent);
            }
        });
        Habitats.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GamesMenuActivity.this, HabitatsGameActivity.class);
                // start the activity connect to the specified class
                startActivity(intent);
            }
        });
        Matching.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GamesMenuActivity.this, Matching6x6Activity.class);
                // start the activity connect to the specified class
                startActivity(intent);
            }
        });
        MyZoo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GamesMenuActivity.this, MyZooActivity.class);
                // start the activity connect to the specified class
                startActivity(intent);
            }
        });
    }
}
