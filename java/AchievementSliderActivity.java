package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AchievementSliderActivity extends AppCompatActivity {
    private final String PREFS_KEY = "highScore";
    private final String score_key = "Matching High Score";
    private final String score_key_h = "Habitats High Score";
    private int[] achievements;
    private Set<String> eagle_canyon, african_rainforest, serengeti_predators, african_savanna;
    // creating object of ViewPager
    ViewPager mViewPager;

    // images array
    int[] images = {R.drawable.slider_instructions, R.drawable.african_bullfrog_badge, R.drawable.african_crested_porcupine_badge, R.drawable.african_lungfish_badge, R.drawable.african_painted_dog_badge,
            R.drawable.african_red_billed_hornbill_badge, R.drawable.african_rock_python_badge, R.drawable.african_slender_snouted_crocodile_badge, R.drawable.african_spurred_tortoise_badge,
            R.drawable.allen_s_swamp_monkey_badge, R.drawable.american_beaver_badge, R.drawable.american_black_bear_badge, R.drawable.amur_tiger_badge, R.drawable.asian_elephant_badge, R.drawable.bald_eagle_badge,
            R.drawable.black_and_white_ruffed_lemur_badge, R.drawable.black_rhinoceros_badge, R.drawable.bontebok_badge, R.drawable.bufflehead_badge, R.drawable.bull_trout_badge, R.drawable.california_condor_badge,
            R.drawable.cattle_egret_badge, R.drawable.cheetah_badge, R.drawable.chimpanzee_badge, R.drawable.chinook_salmon_badge, R.drawable.cinnamon_teal_badge, R.drawable.coho_salmon_badge, R.drawable.colobus_monkey_badge};

    private int loadAniCT(){
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        int aniCT = scorePrefs.getInt("Animals Collected", 0);
        return aniCT;
    }

    private void initializeHabitatSets(){
        eagle_canyon = new HashSet<String>();
        african_rainforest = new HashSet<String>();
        serengeti_predators = new HashSet<String>();
        african_savanna = new HashSet<String>();
        eagle_canyon.add("Bald Eagle"); eagle_canyon.add("Bull Trout"); eagle_canyon.add("Chinook Salmon"); eagle_canyon.add("Coho Salmon"); eagle_canyon.add("Rainbow Trout"); eagle_canyon.add("White Sturgeon");
        african_rainforest.add("African Bullfrog"); african_rainforest.add("African Crested Porcupine"); african_rainforest.add("African Lungfish"); african_rainforest.add("African Slender-Snouted Crocodile"); african_rainforest.add("Allen's Swamp Monkey"); african_rainforest.add("Colobus Monkey"); african_rainforest.add("Rodrigues Flying Fox"); african_rainforest.add("Straw-Colored Fruit Bat");
        african_savanna.add("African Spurred Tortoise"); african_savanna.add("Bontebok"); african_savanna.add("De Brazza's Monkey"); african_savanna.add("Giraffe"); african_savanna.add("Naked Mole Rat"); african_savanna.add("Southern Ground Hornbill"); african_savanna.add("Speke's Gazella");
        serengeti_predators.add("African Painted Dog"); serengeti_predators.add("African Red-Billed Hornbill"); serengeti_predators.add("Black and White-Ruffed Lemur"); serengeti_predators.add("Cheetah"); serengeti_predators.add("Dwarf Mongoose"); serengeti_predators.add("Lion"); serengeti_predators.add("Ring-Tailed Lemur"); serengeti_predators.add("Veiled Chameleon");
    }
    private int loadHighScore(){
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        int highScore = scorePrefs.getInt("High Score", 0);
        return highScore;
    }

    private int loadMatchingHighScore(){
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        int highScore = scorePrefs.getInt(score_key, 0);
        return highScore;
    }

    private int loadHabitatsHighScore(){
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        int highScore = scorePrefs.getInt(score_key_h, 0);
        return highScore;
    }

    /*private int loadMyZooHighScore(){
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        int highScore = scorePrefs.getInt("MyZoo High Score", 0);
        return highScore;
    }*/

    private Set<String> loadCollectionStrings(){
        Set<String> setS = new HashSet<String>();
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        Set<String> ss = scorePrefs.getStringSet("Animal Set", setS);
        System.out.println(ss);
        return ss;
    }
    //int MyZooCT,
    private int[] myAchievements(int AniCT, int QuizCT, Set<String> ss, int MatchingCT, int HabitatsCT){
        ArrayList<Integer> achievements = new ArrayList<Integer>();
        achievements.add(images[0]);
        if (AniCT >= 5){
            achievements.add(images[1]);
            if (AniCT >= 15){
                achievements.add(images[2]);
                if(AniCT >= 34){
                    achievements.add(images[3]);
                    if (AniCT >= 68){
                        achievements.add(images[4]);
                    }
                }
            }
        }
        if(QuizCT >= 25){
            achievements.add(images[5]);
            if (QuizCT >= 100){
                achievements.add(images[6]);
            }
        }
        if(HabitatsCT >= 10){
            achievements.add(images[7]);
            if (HabitatsCT >= 25){
                achievements.add(images[8]);
            }
        }
        if(MatchingCT <=120){
            achievements.add(images[9]);
            if (MatchingCT <= 30){
                achievements.add(images[10]);
            }
        }
        /*if(MyZooCT >= 15){
            achievements.add(images[11]);
            if (MyZooCT >= 30){
                achievements.add(images[12]);
            }
        }*/
        if(ss.containsAll(eagle_canyon)){
            achievements.add(images[13]);
        }
        if(ss.containsAll(african_rainforest)){
            achievements.add(images[14]);
        }
        if(ss.containsAll(african_savanna)){
            achievements.add(images[15]);
        }
        if(ss.containsAll(serengeti_predators)){
            achievements.add(images[16]);
        }
        int[] temp = new int[achievements.size()];
        for(int i = 0; i < achievements.size(); i++){
            temp[i] = achievements.get(i);
        }
        return temp;
    }

    // Creating Object of ViewPagerAdapter
    ViewPagerAdapter mViewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achievement_fragment_screen_slide);
        initializeHabitatSets();
        //loadHabitatsHighScore(), loadMatchingHighScore(), loadMyZooHighScore(),
        achievements = myAchievements(loadAniCT(), loadHighScore(), loadCollectionStrings(), loadMatchingHighScore(), loadHabitatsHighScore());

        // Initializing the ViewPager Object
        mViewPager = (ViewPager)findViewById(R.id.viewPagerMain);

        // Initializing the ViewPagerAdapter
        mViewPagerAdapter = new ViewPagerAdapter(AchievementSliderActivity.this, achievements);

        // Adding the Adapter to the ViewPager
        mViewPager.setAdapter(mViewPagerAdapter);
    }
}
