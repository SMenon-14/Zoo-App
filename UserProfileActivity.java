package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {
    private Context context = this;
    private final String PREFS_KEY = "highScore";
    private final String score_key = "Matching High Score";
    private final String score_key_h = "Habitats High Score";
    private Switch editMode;
    private EditText inputName;
    private Button displayName, changeAvatar, menu, badges, myAnimals;
    private TextView quizHighScore, animalsCollected, matchingHighScore, habitatsHighScore;
    private ImageView avatar;
    private ColorPack red, pink, purple, green, yellow, blue;
    private HashMap<Integer, ColorPack> avatar_colors;
    private int num_clicks_change_avatar;
    private boolean editing;
    private HashMap<String,String> nameDescMap, nameImgMap;

    private void initializeAll(){
        editMode = findViewById(R.id.switch1);
        inputName = findViewById(R.id.edit_name);
        displayName = findViewById(R.id.avatar_name);
        quizHighScore = findViewById(R.id.quiz_high_score);
        matchingHighScore = findViewById(R.id.matching_high_score);
        habitatsHighScore = findViewById(R.id.habitats_high_score);
        animalsCollected = findViewById(R.id.collected_animals);
        avatar = findViewById(R.id.avatar_display);
        changeAvatar = findViewById(R.id.change_avatar);
        menu = findViewById(R.id.profile_menu_button);
        badges = findViewById(R.id.toBadges);
        myAnimals = findViewById(R.id.toAnimalCollection);
    }

    public void readAnimalData() {
        nameDescMap = new HashMap<String, String>();
        nameImgMap = new HashMap<String, String>();

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
            }
        } catch (IOException e) {

            Log.wtf("MyActivity","Error reading data file on line "+line, e);
            e.printStackTrace();
        }
    }

    private void makeColors(){
        avatar_colors = new HashMap<>();
        red = new ColorPack("red", 255, 244, 54, 67);
        blue = new ColorPack("blue", 255, 0, 212, 188);
        green = new ColorPack("green", 255,0,136,150);
        pink = new ColorPack ("pink", 255, 255,142,84);
        purple = new ColorPack("purple", 255,147,239,96);
        yellow = new ColorPack("yellow", 255,255,7,193);
        avatar_colors.put(0, blue);
        avatar_colors.put(1, green);
        avatar_colors.put(2, pink);
        avatar_colors.put(3, purple);
        avatar_colors.put(4, yellow);
        avatar_colors.put(5, red);
    }

    private void goingToGames(){
        Intent intent = new Intent(UserProfileActivity.this, GamesMenuActivity.class);
        intent.putExtra("AnimalImages", nameImgMap);
        intent.putExtra("AnimalDescriptions", nameDescMap);
        startActivity(intent);
    }

    private void goingToIdentifier(){
        Intent intent = new Intent(UserProfileActivity.this, IdentifierActivity.class);
        intent.putExtra("AnimalImages", nameImgMap);
        intent.putExtra("AnimalDescriptions", nameDescMap);
        startActivity(intent);
    }

    private void reloadProfile(){
        Intent intent = new Intent(UserProfileActivity.this, UserProfileActivity.class);
        intent.putExtra("AnimalImages", nameImgMap);
        intent.putExtra("AnimalDescriptions", nameDescMap);
        startActivity(intent);
    }

    private void goingToRandom(){
        Intent intent = new Intent(UserProfileActivity.this, RandomAnimalActivity.class);
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

        Games.setOnClickListener(new View.OnClickListener(){
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
                reloadProfile();
            }
        });

        RandomAnimal.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goingToRandom();
            }
        });
    }

    private void hideEditors(){
        displayName.setEnabled(false);
        quizHighScore.setVisibility(View.VISIBLE);
        habitatsHighScore.setVisibility(View.VISIBLE);
        animalsCollected.setVisibility(View.VISIBLE);
        matchingHighScore.setVisibility(View.VISIBLE);
        myAnimals.setVisibility(View.VISIBLE);
        badges.setVisibility(View.VISIBLE);
        inputName.setVisibility(View.GONE);
        changeAvatar.setVisibility(View.GONE);
    }

    private void openEditors(){
        quizHighScore.setVisibility(View.GONE);
        habitatsHighScore.setVisibility(View.GONE);
        animalsCollected.setVisibility(View.GONE);
        matchingHighScore.setVisibility(View.GONE);
        myAnimals.setVisibility(View.GONE);
        badges.setVisibility(View.GONE);
        inputName.setVisibility(View.VISIBLE);
        changeAvatar.setVisibility(View.VISIBLE);
    }

    private void setEditMode(){
        openEditors();
        changeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random r = new Random();
                int key = r.nextInt(5);
                while (key == num_clicks_change_avatar) {
                    key = r.nextInt(5);
                }
                ColorPack cp = avatar_colors.get(key);
                num_clicks_change_avatar = key;
                avatar.setColorFilter(Color.argb(cp.getAlpha(), cp.getRed(), cp.getGreen(), cp.getBlue()));
            }
        });
    }

    private int loadHabitatsHighScore(){
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        int highScore = scorePrefs.getInt(score_key_h, 0);
        return highScore;
    }

    private int loadMatchingHighScore(){
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        int highScore = scorePrefs.getInt(score_key, 0);
        return highScore;
    }

    private int loadHighScore(){
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        int highScore = scorePrefs.getInt("High Score", 0);
        return highScore;
    }

    private String loadUsername(){
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        String username = scorePrefs.getString("User Name", "");
        return username;
    }

    private int loadAniCT(){
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        int aniCT = scorePrefs.getInt("Animals Collected", 0);
        return aniCT;
    }

    private int loadAvatarResource(){
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        int avatarResource = scorePrefs.getInt("Avatar Int", 0);
        return avatarResource;
    }

    private void savePreferences(String username){
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = scorePrefs.edit();
        editor.putString("User Name", username);
        editor.putInt("Avatar Int", num_clicks_change_avatar);
        editor.commit();
    }

    private void setScreen(String s, int i, int j, int k, int l, int m){
        displayName.setText(s);
        quizHighScore.setText("Quiz High Score: "+Integer.toString(i));
        animalsCollected.setText("Animals Collected: "+Integer.toString(j));
        int minutes = (l/60);
        l = l % 60;
        String time = String.format("%d:%02d", minutes, l);
        matchingHighScore.setText("Matching High Score: "+time);
        habitatsHighScore.setText("Habitats High Score: "+Integer.toString(m));
        ColorPack cp = avatar_colors.get(k);
        avatar.setColorFilter(Color.argb(cp.getAlpha(), cp.getRed(), cp.getGreen(), cp.getBlue()));
    }



    private void unsavedWarning(){
        new AlertDialog.Builder(context)
                .setTitle("Close Profile")
                .setMessage("You haven't switched off edit mode, your changes will not be saved.")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        menuView();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Intent intent = getIntent();
        if (intent.hasExtra("AnimalImages") && intent.hasExtra("AnimalDescriptions")){
            nameImgMap = (HashMap<String, String>) intent.getSerializableExtra("AnimalImages");
            nameDescMap = (HashMap<String, String>) intent.getSerializableExtra("AnimalDescriptions");
        }
        else{
            readAnimalData();
        }
        initializeAll();
        makeColors();
        String s = loadUsername();
        int i = loadHighScore();
        int j = loadAniCT();
        int k = loadAvatarResource();
        int l = loadMatchingHighScore();
        int m = loadHabitatsHighScore();
        setScreen( s, i, j, k, l, m);

        hideEditors();
        inputName.bringToFront();
        editMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setEditMode();
                    editing = true;
                }
                else{
                    editing = false;
                    String name = inputName.getText().toString();
                    if (name.length() > 1) {
                        displayName.setText(name);
                        savePreferences(name);
                        hideEditors();
                    }
                    else{
                        displayName.setText(s);
                        savePreferences(s);
                        hideEditors();
                    }
                }
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editing){
                    unsavedWarning();
                }
                else{
                    menuView();
                }
            }
        });
        badges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editing){
                    unsavedWarning();
                }
                else{
                    Intent intent = new Intent(UserProfileActivity.this, AchievementSliderActivity.class);
                    startActivity(intent);
                }
            }
        });

        myAnimals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editing){
                    unsavedWarning();
                }
                else{
                    Intent intent = new Intent(UserProfileActivity.this, MyAnimalsActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}
