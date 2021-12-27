package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {
    private Context context = this;
    private ImageView toGuess;
    private TextView score, strike;
    private Button toMenu, hints, next, save;
    private Button op1, op2, op3, op4;
    private String txt1, txt2, txt3, txt4;
    private String descripHint;
    private final String PREFS_KEY = "highScore";
    private int points, strikes;
    private int saved;
    private QuestionPack question;
    public ArrayList<Integer> optionList;
    private HashMap<String, String> descMap, imgMap;
    public HashMap<String, Animal> aniMap;
    private MediaPlayer gameOverSound, wonRoundSound, lostRoundSound;

    private void initializeElements(){
        toMenu = findViewById(R.id.open_menu);
        hints = findViewById(R.id.GetHint);
        save = findViewById(R.id.save_button);
        op1 = findViewById(R.id.option1);
        op2 = findViewById(R.id.option2);
        op3 = findViewById(R.id.option3);
        op4 = findViewById(R.id.option4);
        next = findViewById(R.id.nextRD);
        toGuess = findViewById(R.id.imageView);
        score = findViewById(R.id.scoreCT);
        strike = findViewById(R.id.strikeCT);
        optionList = new ArrayList<>();
        gameOverSound = MediaPlayer.create(this, R.raw.game_over_music);
        lostRoundSound = MediaPlayer.create(this, R.raw.lost_round_music);
        wonRoundSound = MediaPlayer.create(this, R.raw.won_round_music);
        points = 0;
        strikes = 0;
        saved = 0;
    }

    private void makeAniMap(){
        int ct = 0;
        for (String s : imgMap.keySet()){
            aniMap.put(Integer.toString(ct), new Animal(s, descMap.get(s), imgMap.get(s)));
            ct++;
        }
    }

    private void resetScreen(){
        op1.setText("");
        op2.setText("");
        op3.setText("");
        op4.setText("");
        optionList.clear();
    }

    private void totalReset(){
        resetScreen();
        points = 0;
        strikes = 0;
        score.setText("Score: 0");
        strike.setText("Strikes: 0");
    }

    private void changeScoreView(){
        score.setText("Score: "+Integer.toString(points));
    }

    private void changeStrikeView(){
        strike.setText("Strikes: "+Integer.toString(strikes));
    }

    private Animal getRandAnimal(){
        Random r = new Random();
        int keyVal = r.nextInt(67);
        while (optionList.contains(keyVal)) {
            keyVal = r.nextInt(67);
        }
        String toCheck = Integer.toString(keyVal);
        Animal a = aniMap.get(toCheck);
        optionList.add(keyVal);
        return a;
    }

    private void enableButtons(){
        op1.setEnabled(true);
        op2.setEnabled(true);
        op3.setEnabled(true);
        op4.setEnabled(true);
    }

    private void disableButtons(){
        op1.setEnabled(false);
        op2.setEnabled(false);
        op3.setEnabled(false);
        op4.setEnabled(false);
    }

    private void buildQuestion(){
        Animal one = getRandAnimal();
        Animal two = getRandAnimal();
        Animal three = getRandAnimal();
        Animal four = getRandAnimal();
        question = new QuestionPack(one, two, three, four);
        descripHint = descMap.get(question.getAnswer());
    }

    private void setView(){
        int resID = getResources().getIdentifier(question.getImagePath(), "drawable",  getPackageName());
        toGuess.setImageResource(resID);
        op1.setText((question.getOption1()).replaceAll("\\s"," \n"));
        op2.setText((question.getOption2()).replaceAll("\\s"," \n"));
        op3.setText((question.getOption3()).replaceAll("\\s"," \n"));
        op4.setText((question.getOption4()).replaceAll("\\s"," \n"));
    }

    private void setTxts(){
        txt1 = question.getOption1();
        txt2 = question.getOption2();
        txt3 = question.getOption3();
        txt4 = question.getOption4();
    }

    private void playRound() {
        enableButtons();
        buildQuestion();
        setView();
        setTxts();
    }

    private void gameOver(){
        strikes++;
        changeStrikeView();
        gameOverSound.start();
        totalReset();
        toGuess.setImageResource(R.drawable.game_over_sign);
    }

    private void wonRound(){
        points++;
        changeScoreView();
        resetScreen();
        toGuess.setImageResource(R.drawable.correct_img);
        wonRoundSound.start();
        next.setText("Play Next Round");
    }

    private void lostRound(){
        lostRoundSound.start();
        strikes++;
        changeStrikeView();
        resetScreen();
        toGuess.setImageResource(R.drawable.incorrect_img);
        next.setText("Play Next Round");
    }

    private void afterAnswered(String btnTXT){
        disableButtons();
        if (btnTXT == question.getAnswer()){
            wonRound();
        }
        else if (strikes == 2){
            saveHighScore();
            gameOver();
        }
        else{
            lostRound();
        }
    }

    private void reloadQuizGame(HashMap images, HashMap descriptions){
        Intent intent = new Intent(QuizActivity.this, GamesMenuActivity.class);
        intent.putExtra("AnimalImages", images);
        intent.putExtra("AnimalDescriptions", descriptions);
        startActivity(intent);
    }

    private void goingToIdentifier(HashMap images, HashMap descriptions){
        Intent intent = new Intent(QuizActivity.this, IdentifierActivity.class);
        intent.putExtra("AnimalImages", images);
        intent.putExtra("AnimalDescriptions", descriptions);
        startActivity(intent);
    }

    private void goingToRandom(HashMap images, HashMap descriptions){
        Intent intent = new Intent(QuizActivity.this, RandomAnimalActivity.class);
        intent.putExtra("AnimalImages", images);
        intent.putExtra("AnimalDescriptions", descriptions);
        startActivity(intent);
    }

    private void goingToProfile(HashMap images, HashMap descriptions){
        Intent intent = new Intent(QuizActivity.this, UserProfileActivity.class);
        intent.putExtra("AnimalImages", images);
        intent.putExtra("AnimalDescriptions", descriptions);
        startActivity(intent);
    }

    private void menuView(){
        setContentView(R.layout.activity_menu);
        Button Games = findViewById(R.id.game_button);
        Button Identifier = findViewById(R.id.image_selection_button);
        Button RandomAnimal = findViewById(R.id.random_animal_button);
        Button Profile = findViewById(R.id.user_profile_button);

        Profile.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goingToProfile(imgMap, descMap);
            }
        });

        Games.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadQuizGame(imgMap, descMap);
            }
        });

        Identifier.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) { goingToIdentifier(imgMap, descMap); }
        });

        RandomAnimal.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) { goingToRandom(imgMap, descMap); }
        });
    }

    private void saveHighScore(){
        saved++;
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = scorePrefs.edit();
        if (loadHighScore() < points) {
            editor.putInt("High Score", points);
            editor.commit();
        }
    }

    private int loadHighScore(){
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        int highScore = scorePrefs.getInt("High Score", 0);
        return highScore;
    }

    private void endGameWarning(){
        new AlertDialog.Builder(context)
                .setTitle("End Game")
                .setMessage("Are you sure you want to end this game? Your score will not be saved")

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
        setContentView(R.layout.activity_quiz);
        initializeElements();
        next.setText("");

        Intent intent = getIntent();
        imgMap = (HashMap<String, String>) intent.getSerializableExtra("AnimalImages");
        descMap = (HashMap<String, String>) intent.getSerializableExtra("AnimalDescriptions");
        aniMap = new HashMap<>();
        makeAniMap();

        playRound();


        op1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afterAnswered(txt1);
            }
        });
        op2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afterAnswered(txt2);
            }
        });
        op3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afterAnswered(txt3);
            }
        });
        op4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afterAnswered(txt4);
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saved > 0){
                    saved = 0;
                }
                playRound();
            }
        });

        toMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saved == 0) {
                    endGameWarning();
                }
                else{
                    menuView();
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveHighScore();
            }
        });

        hints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(context, descripHint, Toast.LENGTH_SHORT);
                t.show();
            }
        });

    }

}
