package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class HabitatsGameActivity extends AppCompatActivity {
    Context context = this;
    private final String PREFS_KEY = "highScore";
    private final String score_key = "Habitats High Score";
    LinearLayout layout;
    Button toGameMenu, playAgain;
    PopupWindow liabilityPopUp;
    private TextView habitat1text, habitat2text, habitat3text, habitat4text, animalToDrag, t, scoreShow, strikesShow, tv;
    private HashMap<Integer, Habitat> habitatMap;
    private ArrayList<String> habitatsUsed;
    private int score = 0;
    private int strikes = 0;

    private void initializeAll(){
        habitatMap = new HashMap<>();
        habitatsUsed = new ArrayList<>();
        habitat1text = (TextView) findViewById(R.id.habitatOp1Text);
        habitat2text = (TextView) findViewById(R.id.habitatOp2Text);
        habitat3text = (TextView) findViewById(R.id.habitatOp3Text);
        habitat4text = (TextView) findViewById(R.id.habitatOp4Text);
        animalToDrag = (TextView) findViewById(R.id.toDrag);
        scoreShow = (TextView) findViewById(R.id.habitats_score);
        strikesShow = (TextView) findViewById(R.id.habitats_strike);
    }

    private int loadHighScore(){
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        int highScore = scorePrefs.getInt(score_key, 0);
        return highScore;
    }

    private void saveHighScore(int habitatsScore){
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = scorePrefs.edit();
        if (loadHighScore() < habitatsScore) {
            editor.putInt(score_key, habitatsScore);
            editor.commit();
        }
    }

    private void setUpPopup(){
        liabilityPopUp = new PopupWindow(context);
        liabilityPopUp.setBackgroundDrawable(null);
        liabilityPopUp.setAnimationStyle(R.style.popup_window_animation);
        layout = new LinearLayout(context);
        layout.setBackgroundResource(R.drawable.gradient_drawable_3);
        tv = new TextView(context);
        LinearLayout.LayoutParams objectParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        layout.setOrientation(LinearLayout.VERTICAL);
        tv.setLayoutParams(objectParams);
        tv.setText("Well Done!");
        tv.setTextSize(36);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.argb(255, 0,0,0));
        toGameMenu = new Button(context);
        toGameMenu.setText("Leave Game");
        playAgain = new Button(context);
        playAgain.setText("Play Again");
        layout.addView(tv);
        layout.addView(toGameMenu);
        layout.addView(playAgain);
    }

    private void playAgain(){
        liabilityPopUp.dismiss();
        Intent intent = new Intent(HabitatsGameActivity.this, HabitatsGameActivity.class);
        startActivity(intent);
    }

    private void toGameMenu(){
        liabilityPopUp.dismiss();
        Intent intent = new Intent(HabitatsGameActivity.this, UserProfileActivity.class);
        startActivity(intent);
    }

    private void onEndGame(){
        setUpPopup();
        loadHighScore();
        saveHighScore(score);
        tv.setText("Well Done! \n"+"Your Score: "+score);
        liabilityPopUp.setContentView(layout);
        liabilityPopUp.showAtLocation(layout, Gravity.LEFT, 50, 10);
        liabilityPopUp.update(50, 50, 600, 800);
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAgain();
            }
        });
        toGameMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toGameMenu();
            }
        });
    }

    private void updateScoreAndStrikes(){
        scoreShow.setText("Score: "+Integer.toString(score));
        strikesShow.setText("Strikes: "+Integer.toString(strikes));
    }


    private void readHabitatData() {
        InputStream is = getResources().openRawResource(R.raw.habitats);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";
        int ct = 0;

        try {
            reader.readLine();
            while ((line = reader.readLine()) != null) {

                String [] tokens = line.split(",");
                String aniName = tokens[0];
                String habitat = tokens[1];
                String red = tokens[2];
                String green = tokens[3];
                String blue = tokens[4];
                habitatMap.put(ct, new Habitat(aniName, habitat, Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue)));
                ct++;
            }
        } catch (IOException e) {

            Log.wtf("MyActivity","Error reading data file on line "+line, e);
            e.printStackTrace();
        }
    }

    private Habitat getRandHabitat(){
        Random r = new Random();
        int keyVal = r.nextInt(67);
        while (habitatsUsed.contains(habitatMap.get(keyVal).getHabitat())){
            keyVal = r.nextInt(67);
        }
        Habitat h = habitatMap.get(keyVal);
        habitatsUsed.add(h.getHabitat());
        return h;
    }

    private void makeBoard(){
        habitatsUsed.clear();
        System.out.println("Into Make Board");
        HabitatBoard hb = new HabitatBoard(getRandHabitat(), getRandHabitat(), getRandHabitat(), getRandHabitat(), habitat1text, habitat2text, habitat3text, habitat4text);
        System.out.println("Made HabitatBoard");
        animalToDrag.setText(hb.getAnimalName());
        animalToDrag.setContentDescription(hb.getHabitatNameAnswer());
        hb.getOne().setText(hb.getHabitatName1());
        hb.getTwo().setText(hb.getHabitatName2());
        hb.getThree().setText(hb.getHabitatName3());
        hb.getFour().setText(hb.getHabitatNameAnswer());
        hb.getOne().setContentDescription(hb.getHabitatName1());
        hb.getTwo().setContentDescription(hb.getHabitatName2());
        hb.getThree().setContentDescription(hb.getHabitatName3());
        hb.getFour().setContentDescription(hb.getHabitatNameAnswer());
        hb.getOne().setBackgroundColor(hb.getHabitatBackground1());
        hb.getTwo().setBackgroundColor(hb.getHabitatBackground2());
        hb.getThree().setBackgroundColor(hb.getHabitatBackground3());
        hb.getFour().setBackgroundColor(hb.getHabitatBackgroundAnswer());
    }

    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v){
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, myShadowBuilder, v, 0);
            return true;
        }

    };

    View.OnDragListener dragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int dragEvent = event.getAction();
            switch (dragEvent){
                case DragEvent.ACTION_DRAG_ENTERED:
                    t = findViewById(v.getId());
                    t.setText("Drop in "+t.getContentDescription());
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    t = findViewById(v.getId());
                    t.setText(t.getContentDescription());
                    break;
                case DragEvent.ACTION_DROP:
                    t = findViewById(v.getId());
                    if (t.getContentDescription() == animalToDrag.getContentDescription()){
                        score++;
                    }
                    else{
                        strikes++;
                        score--;
                    }
                    updateScoreAndStrikes();
                    if(strikes == 5){
                        onEndGame();
                        break;
                    }
                    makeBoard();
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habitats_game_2);
        initializeAll();
        readHabitatData();
        makeBoard();
        animalToDrag.setOnLongClickListener(longClickListener);
        habitat1text.setOnDragListener(dragListener);
        habitat2text.setOnDragListener(dragListener);
        habitat3text.setOnDragListener(dragListener);
        habitat4text.setOnDragListener(dragListener);

    }
}