package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;

public class RandomAnimalActivity extends AppCompatActivity {
    private Context context = this;
    private final String PREFS_KEY = "highScore";
    private ImageView animalImg;
    private Button getRandom, toMenu;
    private TextView animalName, animalDescription;
    private HashMap<String, Animal> aniMap;
    public HashMap<String, String> nameImgMap, nameDescMap;
    private Set<String> stringSet;


    private void initializeAll(){
        animalImg = (ImageView)findViewById(R.id.animalView);
        animalName = (TextView)findViewById(R.id.animalName);
        animalDescription = (TextView)findViewById(R.id.animalDescription);
        getRandom = (Button)findViewById(R.id.Randomize);
        toMenu = (Button)findViewById(R.id.menu_button);
    }

    private Set<String> loadCollectionStrings(){
        Set<String> setS = new HashSet<String>();
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        Set<String> ss = scorePrefs.getStringSet("Animal Ints", setS);
        return ss;
    }

    private void makeAniMap(){
        int ct = 0;
        for (String s : nameImgMap.keySet()){
            aniMap.put(Integer.toString(ct), new Animal(s, nameDescMap.get(s), nameImgMap.get(s)));
            ct++;
        }
    }

    public void readAnimalData() {
        nameDescMap = new HashMap<String, String>();
        nameImgMap = new HashMap<String, String>();
        aniMap = new HashMap<String, Animal>();
        int count = 0;

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
                nameImgMap.put(aniName, imgName);
                nameDescMap.put(aniName, aniDesc);
                aniMap.put(Integer.toString(count), new Animal(aniName, aniDesc, imgName));
                count++;
            }
        } catch (IOException e) {

            Log.wtf("MyActivity","Error reading data file on line "+line, e);
            e.printStackTrace();
        }
    }

    private void showAnimal(Animal a){

        animalName.setText(a.getName());
        animalDescription.setText(a.getDesc());
        int resID = getResources().getIdentifier(a.getIM(), "drawable",  getPackageName());
        animalImg.setImageResource(resID);
    }

    private void fetchAnimal(String name){
        name = name.replaceAll("\\s", "").toLowerCase();
        for(String s : aniMap.keySet()){
            Animal a = aniMap.get(s);
            String n = a.getName().replaceAll("\\s", "").toLowerCase();
            if (name.contains(n)){
                stringSet.add(Integer.toString(a.getID()));
                showAnimal(a);
            }
        }
    }

    private void pickRandAnimal() {
        Random r = new Random();
        int keyVal = r.nextInt(67);
        String toCheck = Integer.toString(keyVal);
        for (String s : aniMap.keySet()) {
            if (s == toCheck) {
                Animal a = aniMap.get(s);
                showAnimal(a);
                break;
            }
        }
    }

    private void goingToGames(){
        Intent intent = new Intent(RandomAnimalActivity.this, GamesMenuActivity.class);
        intent.putExtra("AnimalImages", nameImgMap);
        intent.putExtra("AnimalDescriptions", nameDescMap);
        startActivity(intent);
    }

    private void goingToIdentifier(){
        Intent intent = new Intent(RandomAnimalActivity.this, IdentifierActivity.class);
        intent.putExtra("AnimalImages", nameImgMap);
        intent.putExtra("AnimalDescriptions", nameDescMap);
        startActivity(intent);
    }

    private void goingToProfile(){
        Intent intent = new Intent(RandomAnimalActivity.this, UserProfileActivity.class);
        intent.putExtra("AnimalImages", nameImgMap);
        intent.putExtra("AnimalDescriptions", nameDescMap);
        startActivity(intent);
    }

    private void reloadRandom(){
        Intent intent = new Intent(RandomAnimalActivity.this, RandomAnimalActivity.class);
        intent.putExtra("AnimalImages", nameImgMap);
        intent.putExtra("AnimalDescriptions", nameDescMap);
        startActivity(intent);
    }

    private void menuView(){
        setContentView(R.layout.activity_menu);
        Button Games = findViewById(R.id.game_button);
        Button Identifier = findViewById(R.id.image_selection_button);
        Button RandomAnimal = findViewById(R.id.random_animal_button);
        Button Profile = findViewById(R.id.user_profile_button);

        Games.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goingToGames();
            }
        });

        Identifier.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goingToIdentifier();
            }
        });

        Profile.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goingToProfile();
            }
        });

        RandomAnimal.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadRandom();
            }
        });
    }

    public HashMap<String, Animal> getMap() {
        return aniMap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_display);
        Intent intent = getIntent();
        if (intent.hasExtra("AnimalImages") && intent.hasExtra("AnimalDescriptions")){
            nameImgMap = (HashMap<String, String>) intent.getSerializableExtra("AnimalImages");
            nameDescMap = (HashMap<String, String>) intent.getSerializableExtra("AnimalDescriptions");
            aniMap = new HashMap<>();
            makeAniMap();
        }
        else{
            readAnimalData();
        }
        initializeAll();

        if (intent.hasExtra("Animal")) {
            String toSet = intent.getStringExtra("Animal");
            stringSet = loadCollectionStrings();
            fetchAnimal(toSet);
            getRandom.setVisibility(View.GONE);
            SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
            SharedPreferences.Editor editor = scorePrefs.edit();
            editor.putStringSet("Animal Ints", stringSet);
            editor.commit();
        }


        getRandom.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickRandAnimal();
            }
        });

        toMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuView();
            }
        });

    }


}
