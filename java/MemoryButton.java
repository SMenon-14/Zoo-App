package com.example.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.appcompat.widget.AppCompatDrawableManager;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

class MemoryButton extends androidx.appcompat.widget.AppCompatButton {
    protected int row;
    protected int column;
    protected int frontDrawableId;

    protected boolean isFlipped = false;
    protected boolean isMatched = false;

    protected Drawable front;
    protected Drawable back;

    public MemoryButton(Context context, int r, int c, int frontImageDrawableID){
        super(context);
        row = r;
        column = c;
        frontDrawableId = frontImageDrawableID;
        front = getDrawable(context, frontImageDrawableID);
        back = getDrawable(context, R.drawable.card_back_2);
        setBackground(back);

        GridLayout.LayoutParams tempParams = new GridLayout.LayoutParams(GridLayout.spec(r), GridLayout.spec(c));

        tempParams.width = (int) getResources().getDisplayMetrics().density * 50;
        tempParams.height = (int) getResources().getDisplayMetrics().density * 50;
        setLayoutParams(tempParams);

    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched){
        isMatched = matched;
    }

    public int getFrontDrawableId(){
        return frontDrawableId;
    }

    public void flip(){
        if (isMatched)
            return;
        if (isFlipped){
            setBackground(back);
            isFlipped = false;
        }
        else{
            setBackground(front);
            isFlipped = true;
        }
    }
}
