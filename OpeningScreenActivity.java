package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class OpeningScreenActivity extends AppCompatActivity {
    LinearLayout mainLayout;
    PopupWindow liabilityPopUp;
    private int click = 1;
    private HashMap<String,String> nameDescMap, nameImgMap;
    private final String recogLiabilities = ("Zoo Learn's image classification is meant for the image recognition of animals & does not\n" +
            "take responsibility for the classification \n" +
            "of non-animal images. The image recognition\n" +
            "model we use is trained only on our 68 classes\n" +
            "of animals and will return whatever class is the closest to your inputted image.");

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
        Intent intent = new Intent(OpeningScreenActivity.this, GamesMenuActivity.class);
        intent.putExtra("AnimalImages", nameImgMap);
        intent.putExtra("AnimalDescriptions", nameDescMap);
        startActivity(intent);
    }

    private void goingToIdentifier(){
        Intent intent = new Intent(OpeningScreenActivity.this, IdentifierActivity.class);
        intent.putExtra("AnimalImages", nameImgMap);
        intent.putExtra("AnimalDescriptions", nameDescMap);
        startActivity(intent);
    }

    private void goingToProfile(){
        Intent intent = new Intent(OpeningScreenActivity.this, UserProfileActivity.class);
        intent.putExtra("AnimalImages", nameImgMap);
        intent.putExtra("AnimalDescriptions", nameDescMap);
        startActivity(intent);
    }

    private void goingToRandom(){
        Intent intent = new Intent(OpeningScreenActivity.this, RandomAnimalActivity.class);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readAnimalData();
        Typeface face = ResourcesCompat.getFont(this, R.font.annie_use_your_telescope);
        liabilityPopUp = new PopupWindow(this);
        liabilityPopUp.setBackgroundDrawable(null);
        liabilityPopUp.setAnimationStyle(R.style.popup_window_animation);
        LinearLayout layout = new LinearLayout(this);
        mainLayout = new LinearLayout(this);
        TextView tv = new TextView(this);
        TextView clickTo = new TextView(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);

        clickTo.setText("Click Anywhere To Continue");
        clickTo.setGravity(Gravity.CENTER);
        clickTo.setTextColor(Color.argb(255,225,152,0));
        clickTo.setTextSize(20);
        clickTo.setTypeface(face);

        tv.setText(recogLiabilities);
        tv.setTextColor(Color.argb(255,0,0,0));
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(face);
        layout.addView(tv, params);
        layout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        layout.setVerticalGravity(Gravity.CENTER_VERTICAL);

        layout.setBackgroundResource(R.drawable.gradient_drawable_2);
        liabilityPopUp.setContentView(layout);

        mainLayout.setBackgroundResource(R.drawable.home_screen_animation);
        mainLayout.addView(clickTo, params);
        mainLayout.setHorizontalGravity(Gravity.CENTER);
        mainLayout.setVerticalGravity(Gravity.BOTTOM);


        setContentView(mainLayout);

        click = 0;

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        clickTo.startAnimation(anim);

        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click == 0){
                    liabilityPopUp.showAtLocation(layout, Gravity.LEFT, 50, 10);
                    liabilityPopUp.update(50, 50, 600, 800);
                    click = 1;
                }
                else if (click == 1){
                    liabilityPopUp.dismiss();
                    click = 2;
                }
                else{
                    menuView();
                }
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (click == 1){
                    liabilityPopUp.dismiss();
                    click = 2;
                }
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        AnimationDrawable screenAnim = (AnimationDrawable) mainLayout.getBackground();
        screenAnim.start();
    }
}
