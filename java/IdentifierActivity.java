package com.example.myapplication;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ml.ZooAnimals;

import org.tensorflow.lite.support.image.TensorImage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.tensorflow.lite.support.label.Category;

public class IdentifierActivity extends AppCompatActivity {
    private Context context = this;
    private TextView status;
    private final String PREFS_KEY = "highScore";
    private final String ANIMAL_SET_KEY = "Animal Set";
    private ImageView display;
    private Button cameraAccess, menu;
    private List<Category> probability;
    public HashMap<String, String> descripMap, imageMap;
    Set<String> setString;
    int collection;

    private void initializeAll(){
        status = findViewById(R.id.animal_status);
        display = findViewById(R.id.click_image);
        cameraAccess = findViewById(R.id.camera_button);
        menu = findViewById(R.id.menu_view_button);
        setString = new HashSet<String>();
    }

    private void getImFromGallery(){
        mGetContent.launch("image/*");
        Toast t = Toast.makeText(context, "Identifying Your Image Might Take A Bit, Do Not Click Image Multiple Times", Toast.LENGTH_LONG);
        t.show();
    }

    private void runTFModel(Bitmap b){
        try {
            ZooAnimals model = ZooAnimals.newInstance(context);

            // Creates inputs for reference.
            TensorImage image = TensorImage.fromBitmap(b);

            // Runs model inference and gets result.
            ZooAnimals.Outputs outputs = model.process(image);
            probability = outputs.getProbabilityAsCategoryList();
            showIdentifiedAnimal();
            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri uri) {
            String uriPath = uri.getPath();
            display.setImageURI(uri);
            Drawable d = display.getDrawable();
            BitmapDrawable bitmapDrawable = ((BitmapDrawable) d);
            Bitmap bit = bitmapDrawable.getBitmap();
            runTFModel(bit);
        }
    });

    private String getClosestAnimal(List<Category> catList){
        float highest = (float) 0.0;
        String closestAnimal = "";
        for (int i = 0; i < catList.size(); i++){
            Category x = catList.get(i);
            float aniXScore = x.getScore();
            if (aniXScore > highest && aniXScore > 0.1){
                highest = aniXScore;
                closestAnimal = x.getLabel();
            }
        }
        return closestAnimal;
    }

    private void showIdentifiedAnimal(){
        String identified = getClosestAnimal(probability);
        if (!setString.contains(identified) && identified != ""){
            setString.add(identified);
            collection++;
        }
        if (identified == ""){
            status.setText("This isn't an animal we know!");
        }
        else {
            status.setText(identified);
            Intent intent = new Intent(IdentifierActivity.this, RandomAnimalActivity.class);
            intent.putExtra("Animal", identified);
            setSavedPreferences();
            startActivity(intent);
        }
    }

    private int loadCollection(){
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        int i = scorePrefs.getInt("Animals Collected", 0);
        return i;
    }

    private Set<String> loadCollectionStrings(){
        Set<String> setS = new HashSet<String>();
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        Set<String> ss = scorePrefs.getStringSet(ANIMAL_SET_KEY, setS);
        return ss;
    }

    private void setSavedPreferences(){
        SharedPreferences scorePrefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = scorePrefs.edit();
            editor.putInt("Animals Collected", collection);
            editor.putStringSet(ANIMAL_SET_KEY, setString);
            editor.commit();
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

    private void reloadIdentifier(){
        Intent intent = new Intent(IdentifierActivity.this, IdentifierActivity.class);
        intent.putExtra("AnimalImages", imageMap);
        intent.putExtra("AnimalDescriptions", descripMap);
        startActivity(intent);
    }

    private void goingToGames(){
        Intent intent = new Intent(IdentifierActivity.this, GamesMenuActivity.class);
        intent.putExtra("AnimalImages", imageMap);
        intent.putExtra("AnimalDescriptions", descripMap);
        startActivity(intent);
    }

    private void goingToRandom(){
        Intent intent = new Intent(IdentifierActivity.this, RandomAnimalActivity.class);
        intent.putExtra("AnimalImages", imageMap);
        intent.putExtra("AnimalDescriptions", descripMap);
        startActivity(intent);
    }

    private void goingToProfile(){
        Intent intent = new Intent(IdentifierActivity.this, UserProfileActivity.class);
        intent.putExtra("AnimalImages", imageMap);
        intent.putExtra("AnimalDescriptions", descripMap);
        startActivity(intent);
    }

    private void menuView(){
        setContentView(R.layout.activity_menu);
        Button Games = findViewById(R.id.game_button);
        Button Identifier = findViewById(R.id.image_selection_button);
        Button RandomAnimal = findViewById(R.id.random_animal_button);
        Button UserProfile = findViewById(R.id.user_profile_button);

        UserProfile.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goingToProfile();
            }
        });

        Games.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goingToGames();
            }
        });

        Identifier.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadIdentifier();
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
        setContentView(R.layout.activity_identifier);
        initializeAll();
        Intent intent = getIntent();
        if (intent.hasExtra("AnimalImages") && intent.hasExtra("AnimalDescriptions")){
            imageMap = (HashMap<String, String>) intent.getSerializableExtra("AnimalImages");
            descripMap = (HashMap<String, String>) intent.getSerializableExtra("AnimalDescriptions");
        }
        else{
            readAnimalData();
        }
        setString = loadCollectionStrings();
        collection = loadCollection();

        cameraAccess.setOnClickListener(view -> {
            getImFromGallery();
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSavedPreferences();
                menuView();
            }
        });

    }

}
