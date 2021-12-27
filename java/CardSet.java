package com.example.myapplication;

import android.widget.ImageButton;
import android.widget.ImageView;

public class CardSet {
    ImageView B;
    ImageView B2;
    Animal A;

    public CardSet(ImageView b, ImageView b2, Animal a){
        B = b;
        B2 = b2;
        A = a;
        B.setContentDescription(A.getName());
        B2.setContentDescription(A.getName());
    }

    public ImageView getB(){
        return B;
    }

    public ImageView getB2(){
        return B2;
    }

    public Animal getA(){
        return A;
    }
}
