package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Matching6x6Activity extends AppCompatActivity implements View.OnClickListener {
    Context context = this;

    private final String PREFS_KEY = "highScore";
    private final String score_key = "Matching High Score";

    LinearLayout layout;

    Button toGameMenu, playAgain;

    TextView tv;

    PopupWindow liabilityPopUp;

    private int numberOfElements, found;

    long endTime, startTime;

    private MemoryButton[] buttons;

    private int[] buttonGraphicLocations, buttonGraphics;

    Integer[] images = {R.drawable.a___african_bullfrog_1_jpg, R.drawable.a___african_crested_porcupine_2_jpg, R.drawable.a___african_lungfish_3_jpg, R.drawable.a___african_painted_dog_4_jpg, R.drawable.a___african_red_billed_hornbill_5_jpg,
            R.drawable.a___african_rock_python_6_jpg, R.drawable.a___african_slender_snouted_crocodile_7_jpg, R.drawable.a___african_spurred_tortoise_8_jpg, R.drawable.a___allen_s_swamp_monkey_9_jpg,
            R.drawable.a___american_beaver_10_jpg, R.drawable.a___american_black_bear_11_jpg, R.drawable.a___amur_tiger_12_jpg, R.drawable.a___asian_elephant_13_jpg, R.drawable.a___bald_eagle_14_jpg, R.drawable.a___black_and_white_ruffed_lemur_15_jpg,
            R.drawable.a___black_rhinoceros_16_jpg, R.drawable.a___bontebok_17_jpg, R.drawable.a___bufflehead_18_jpg, R.drawable.a___bull_trout_19_jpg, R.drawable.a___california_condor_20_jpg, R.drawable.a___cattle_egret_21_jpg,
            R.drawable.a___cheetah_22_jpg, R.drawable.a___chimpanzee_23_jpg, R.drawable.a___chinook_salmon_24_jpg, R.drawable.a___cinnamon_teal_25_jpg, R.drawable.a___coho_salmon_26_jpg, R.drawable.a___colobus_monkey_27_jpg, R.drawable.a___cougar_28_jpg,
            R.drawable.a___de_brazza_s_monkey_29_jpg, R.drawable.a___dwarf_mongoose_30_jpg, R.drawable.a___giraffe_31_jpg, R.drawable.a___grey_gull_32_jpg, R.drawable.a___hadada_ibis_33_jpg, R.drawable.a___harbor_seal_34_jpg, R.drawable.a___hooded_merganser_35_jpg,
            R.drawable.a___humboldt_penguin_36_jpg, R.drawable.a___inca_tern_37_jpg, R.drawable.a___lesser_flamingo_38_jpg, R.drawable.a___lion_39_jpg, R.drawable.a___naked_mole_rat_40_jpg, R.drawable.a___nankin_chicken_41_jpg, R.drawable.a___northern_pintail_42_jpg,
            R.drawable.a___northern_shoveler_43_jpg, R.drawable.a___orangutan_44_jpg, R.drawable.a___pacific_lamprey_45_jpg, R.drawable.a___polar_bear_46_jpg, R.drawable.a___pygmy_goat_47_jpg, R.drawable.a___pygora_goat_48_jpg, R.drawable.a___rainbow_trout_49_jpg,
            R.drawable.a___red_panda_50_jpg, R.drawable.a___red_crested_pochard_51_jpg, R.drawable.a___redhead_52_jpg, R.drawable.a___ring_tailed_lemur_53_jpg, R.drawable.a___ringtail_54_jpg, R.drawable.a___river_otter_55_jpg,R.drawable.a___rocky_mountain_goat_56_jpg,
            R.drawable.a___rodrigues_flying_fox_57_jpg, R.drawable.a___sacred_ibis_58_jpg, R.drawable.a___southern_ground_hornbill_59_jpg, R.drawable.a___southern_sea_otter_60_jpg, R.drawable.a___speke_s_gazelle_61_jpg, R.drawable.a___straw_colored_fruit_bat_62_jpg,
            R.drawable.a___veiled_chameleon_63_jpg, R.drawable.a___western_painted_turtle_64_jpg, R.drawable.a___white_sturgeon_65_jpg, R.drawable.a___white_cheeked_gibbon_66_jpg, R.drawable.a___white_faced_whistling_duck_67_jpg, R.drawable.a___wood_duck_68_jpg};


    private MemoryButton selectedButton1, selectedButton2;

    private boolean isBusy = false;

    private void initializeGrid(GridLayout gridLayout, int columns, int rows){
        numberOfElements = columns * rows;
        buttons = new MemoryButton[numberOfElements];
        buttonGraphics = new int[numberOfElements / 2];
    }

    private void chooseImages(Integer[] images){
        List<Integer> intList = Arrays.asList(images);

        Collections.shuffle(intList);

        intList.toArray(images);

        for (int i = 0; i < (numberOfElements/2); i++){
            buttonGraphics[i] = images[i];
        }
    }

    protected void shuffleButtonGraphics(){
        Random rand  = new Random();
        for (int i = 0; i < numberOfElements; i++){
            buttonGraphicLocations[i] = i % (numberOfElements / 2);
        }
        for (int i = 0; i < numberOfElements; i++){
            int temp = buttonGraphicLocations[i];
            int swapIndex = rand.nextInt(15);
            int tempLoc = buttonGraphicLocations[temp];
            int swapLoc = buttonGraphicLocations[swapIndex];
            buttonGraphicLocations[temp] = swapLoc;
            buttonGraphicLocations[swapIndex] = tempLoc;
        }
    }

    private void putItemsInGrid(GridLayout gridLayout, int r, int c, int columns){
        MemoryButton tempButton = new MemoryButton(this, r, c, buttonGraphics[buttonGraphicLocations[r * columns + c]]);
        tempButton.setId(View.generateViewId());
        tempButton.setOnClickListener(this);
        buttons[r * columns + c] = tempButton;
        gridLayout.addView(tempButton);
    }

    private void saveHighScore(int timeScore){
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = scorePrefs.edit();
        if (loadHighScore() < timeScore) {
            editor.putInt(score_key, timeScore);
            editor.commit();
        }
    }

    private int loadHighScore(){
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        int highScore = scorePrefs.getInt(score_key, 0);
        return highScore;
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
        Intent intent = new Intent(Matching6x6Activity.this, Matching6x6Activity.class);
        startActivity(intent);
    }

    private void toGameMenu(){
        liabilityPopUp.dismiss();
        Intent intent = new Intent(Matching6x6Activity.this, UserProfileActivity.class);
        startActivity(intent);
    }

    private void onEndGame(){
        setUpPopup();
        endTime = System.currentTimeMillis() - startTime;
        int seconds = (int) (endTime/1000);
        loadHighScore();
        saveHighScore(seconds);
        int minutes = (seconds/60);
        seconds = seconds % 60;
        String time = String.format("%d:%02d", minutes, seconds);
        tv.setText("Well Done! \n"+"Your Time: "+time);
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

    @Override
    public void onClick(View view) {
        if (isBusy)
            return;
        MemoryButton button = (MemoryButton) view;
        if (button.isMatched)
            return;
        if (selectedButton1 == null){
            selectedButton1 = button;
            selectedButton1.flip();
            return;
        }
        if (selectedButton1.getId() == button.getId()){
            return;
        }
        if(selectedButton1.getFrontDrawableId() == button.getFrontDrawableId()){
            button.flip();

            button.setMatched(true);
            selectedButton1.setMatched(true);

            selectedButton1.setEnabled(false);
            button.setEnabled(false);

            selectedButton1 = null;

            found++;

            if (found == numberOfElements/2){
                onEndGame();
            }

            return;
        }
        else{
            selectedButton2 = button;
            selectedButton2.flip();
            isBusy = true;

            final Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    selectedButton2.flip();
                    selectedButton1.flip();
                    selectedButton1 = null;
                    selectedButton2 = null;
                    isBusy = false;
                }
            } ,500);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game6x6);
        startTime = System.currentTimeMillis();

        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid_layout);
        int numColumns = gridLayout.getColumnCount();
        int numRows = gridLayout.getRowCount();
        initializeGrid(gridLayout, numColumns, numRows);

        chooseImages(images);

        buttonGraphicLocations = new int[numberOfElements];

        shuffleButtonGraphics();

        for (int r = 0; r < numRows; r++){

            for (int c = 0; c < numColumns; c++){
                putItemsInGrid(gridLayout, r, c, numColumns);
            }
        }
    }


}
