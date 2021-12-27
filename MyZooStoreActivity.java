package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MyZooStoreActivity extends AppCompatActivity {
    Context context = this;
    private final String PREFS_KEY = "highScore";
    private final String zoo_points_key = "Zoo Point CT";
    private final String zoo_icons_set = "Icons Bought";
    private final String my_zoo_icon = "My Zoo Icon";
    private String set_icon;
    private int currPoints;
    private ImageButton closeStore;
    private Button one, two, three, four, five, six, seven, eight, nine, chosen;
    private Set<String> iconsSet;
    private HashMap<String,Integer> iconMap;
    private TextView nextPage;

    private void initializeAll(){
        closeStore = findViewById(R.id.close_store);
        iconsSet = new HashSet<String>();
        iconMap = new HashMap<>();
        nextPage = findViewById(R.id.store_instructions);
        one = findViewById(R.id.tiger_one);
        two = findViewById(R.id.tiger_two);
        three = findViewById(R.id.cheetah_one);
        four = findViewById(R.id.cheetah_two);
        five = findViewById(R.id.cheetah_three);
        six = findViewById(R.id.panda_one);
        seven = findViewById(R.id.panda_two);
        eight = findViewById(R.id.snake_one);
        nine = findViewById(R.id.snake_two);
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

    private void saveIcon(String icon){
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = scorePrefs.edit();
        editor.putString(my_zoo_icon, icon);
        editor.commit();
    }

    private Set<String> loadCollectionStrings(){
        Set<String> setS = new HashSet<String>();
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        Set<String> ss = scorePrefs.getStringSet(zoo_icons_set, setS);
        return ss;
    }

    private void setSavedPreferences(){
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = scorePrefs.edit();
        editor.putStringSet(zoo_icons_set, iconsSet);
        editor.commit();
    }

    private void setBought(Button[] buttons){
        for(int i = 0; i < buttons.length; i++){
            Button b = buttons[i];
            String s = (String) b.getText();
            b.setContentDescription(s);
            if(iconsSet.contains(s)){
                b.setText("Owned");
            }
        }
    }

    View.OnClickListener buyOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button b = findViewById(view.getId());
            String s = (String) b.getText();
            if (s != "Owned") {
                int price = Integer.parseInt(s);
                if (currPoints >= price) {
                    String s1 = (String) b.getContentDescription();
                    currPoints = currPoints - price;
                    iconsSet.add(s1);
                    chosen.setText("Owned");
                    b.setText("Chosen");
                    chosen = b;
                    setSavedPreferences();
                } else{
                    Toast t = Toast.makeText(context, "You don't have enough zoo points", Toast.LENGTH_SHORT);
                    t.show();
                }
            }
            else{
                chosen.setText("Owned");
                b.setText("Chosen");
                chosen = b;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_zoo_store);
        initializeAll();
        currPoints = loadZooPoints();
        chosen = one;
        iconsSet = loadCollectionStrings();
        Button[] buttons= {one, two, three, four, five, six, seven, eight, nine};
        setBought(buttons);
        one.setOnClickListener(buyOnClick);
        two.setOnClickListener(buyOnClick);
        three.setOnClickListener(buyOnClick);
        four.setOnClickListener(buyOnClick);
        five.setOnClickListener(buyOnClick);
        six.setOnClickListener(buyOnClick);
        seven.setOnClickListener(buyOnClick);
        eight.setOnClickListener(buyOnClick);
        nine.setOnClickListener(buyOnClick);

        closeStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_icon = (String) chosen.getContentDescription();
                saveIcon(set_icon);
                saveZooPoints(currPoints);
                Intent intent = new Intent(MyZooStoreActivity.this, MyZooActivity.class);
                startActivity(intent);
            }
        });

        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyZooStoreActivity.this, MyZooBoosters.class);
                startActivity(intent);
            }
        });


    }
}