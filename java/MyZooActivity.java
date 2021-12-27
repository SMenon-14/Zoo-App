package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MyZooActivity extends AppCompatActivity {
    private final String PREFS_KEY = "highScore";
    private final String zoo_points_key = "Zoo Point CT";
    private final String my_zoo_icon = "My Zoo Icon";
    private HashMap<String,String> nameDescMap, nameImgMap;
    private HashMap<String,Integer> iconMap;
    private TextView textView, swipeInstructions, clickInstructions, answerInstructions, pointDisplay;
    private ImageView icon;
    private Button store, menu;
    TrueFalse tf;
    private HashMap <Integer, TrueFalse> qMap;
    private boolean answer = false;
    private int zoo_points;

    private void initializeAll(){
        qMap = new HashMap<>();
        iconMap = new HashMap<>();
        textView = findViewById(R.id.my_zoo_question);
        swipeInstructions = findViewById(R.id.swipe_instructions);
        clickInstructions = findViewById(R.id.click_instructions);
        answerInstructions = findViewById(R.id.swipe_instructions3);
        pointDisplay = findViewById(R.id.score_display);
        store = findViewById(R.id.store_button);
        menu = findViewById(R.id.menu_from_clicker);
        icon = findViewById(R.id.clicker_icon);
    }

    private void setBlinkingText(TextView text) {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        text.startAnimation(anim);
    }

    private void makeIconMap(){
        iconMap.put("50", R.drawable.tiger_1);
        iconMap.put("175", R.drawable.tiger_2);
        iconMap.put("255", R.drawable.cheetah_1);
        iconMap.put("345", R.drawable.cheetah_2);
        iconMap.put("400", R.drawable.cheetah_3);
        iconMap.put("460", R.drawable.panda_1);
        iconMap.put("595", R.drawable.panda_2);
        iconMap.put("755", R.drawable.snake_1);
        iconMap.put("900", R.drawable.snake_2);
    }

    private void setIcon(String key){
        Integer i = iconMap.get(key);
        icon.setImageResource(i);
    }

    private void createQMap(){
        TrueFalse tf1 = new TrueFalse("The First Zoo Was Founded in Paris", true);
        TrueFalse tf2 = new TrueFalse("The First Zoo Was Founded in 1974", false);
        TrueFalse tf3 = new TrueFalse("The First Zoo In the US was in Maine", false);
        TrueFalse tf4 = new TrueFalse("Zoos try to Recreate an Animals Natural Habitat", true);
        TrueFalse tf5 = new TrueFalse("African Bullfrogs eat both Reptiles and Birds", true);
        TrueFalse tf6 = new TrueFalse("Female African Bullfrogs lay between 700-900 eggs", false);
        TrueFalse tf7 = new TrueFalse("Rainbow Trout Spend Their Entire Life Freshwater", true);
        TrueFalse tf8 = new TrueFalse("Male Lions Live Together in Social Groups Called Prides", false);
        TrueFalse tf9 = new TrueFalse("Elephants Eat a Total of Between 10 and 12% of Their Weight Each Day", false);
        TrueFalse tf10 = new TrueFalse("Newborn Polar Bears Weigh Only About a Pound", true);
        qMap.put(0, tf1);
        qMap.put(1, tf2);
        qMap.put(2, tf3);
        qMap.put(3, tf4);
        qMap.put(4, tf5);
        qMap.put(5, tf6);
        qMap.put(6, tf7);
        qMap.put(7, tf8);
        qMap.put(8, tf9);
        qMap.put(9, tf10);
    }

    private void saveZooPoints(int zooPoints){
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = scorePrefs.edit();
        editor.putInt(zoo_points_key, zooPoints);
        editor.commit();
    }

    private int loadZooPoints(){
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        int highScore = scorePrefs.getInt(zoo_points_key, 0);
        return highScore;
    }

    private String loadIcon(){
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        String icon = scorePrefs.getString(my_zoo_icon, "50");
        return icon;
    }

    private TrueFalse getRandQuestion(){
        Random r = new Random();
        int keyVal = r.nextInt(9);
        TrueFalse q = qMap.get(keyVal);
        return q;
    }

    private void setQuestion(){
        tf = getRandQuestion();
        String q = tf.getQuestion();
        textView.setText(q);
    }

    private void ifCorrect(){
        zoo_points+=50;
        pointDisplay.setText("Zoo Points: "+Integer.toString(zoo_points));
    }

    private void ifWrong(){
        zoo_points-=20;
        pointDisplay.setText("Zoo Points: "+Integer.toString(zoo_points));
    }

    private void checkAnswer(){
        if(answer == tf.getVeracity()){
            ifCorrect();
        }
        else{
            ifWrong();
        }

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

    private void goingToGames(){
        Intent intent = new Intent(MyZooActivity.this, GamesMenuActivity.class);
        intent.putExtra("AnimalImages", nameImgMap);
        intent.putExtra("AnimalDescriptions", nameDescMap);
        startActivity(intent);
    }

    private void goingToIdentifier(){
        Intent intent = new Intent(MyZooActivity.this, IdentifierActivity.class);
        intent.putExtra("AnimalImages", nameImgMap);
        intent.putExtra("AnimalDescriptions", nameDescMap);
        startActivity(intent);
    }

    private void goingToProfile(){
        Intent intent = new Intent(MyZooActivity.this, UserProfileActivity.class);
        intent.putExtra("AnimalImages", nameImgMap);
        intent.putExtra("AnimalDescriptions", nameDescMap);
        startActivity(intent);
    }

    private void goingToRandom(){
        Intent intent = new Intent(MyZooActivity.this, RandomAnimalActivity.class);
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
                goingToProfile();
            }
        });

        RandomAnimal.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goingToRandom();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_zoo);
        Intent intent = getIntent();
        initializeAll();
        zoo_points = loadZooPoints();
        pointDisplay.setText("Zoo Points: "+Integer.toString(zoo_points));
        setBlinkingText(swipeInstructions);
        setBlinkingText(clickInstructions);
        setBlinkingText(answerInstructions);
        createQMap();
        makeIconMap();
        setQuestion();

        String iconkey = loadIcon();
        setIcon(iconkey);

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoo_points++;
                pointDisplay.setText("Zoo Points: "+Integer.toString(zoo_points));
            }
        });

        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveZooPoints(zoo_points);
                Intent intent = new Intent(MyZooActivity.this, MyZooStoreActivity.class);
                startActivity(intent);
            }
        });

        textView.setOnTouchListener(new OnSwipeTouchListener(MyZooActivity.this) {
            public void onSwipeLeft() {
                answer = false;
                checkAnswer();
                setQuestion();
            }
            public void onSwipeRight() {
                answer = true;
                checkAnswer();
                setQuestion();
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveZooPoints(zoo_points);
                menuView();
            }
        });
    }
}
