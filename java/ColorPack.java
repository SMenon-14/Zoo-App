package com.example.myapplication;

class ColorPack {
    int alpha;
    int red;
    int blue;
    int green;
    String name;
    public ColorPack(String nameC, int alphaC, int redC, int blueC, int greenC){
        alpha = alphaC;
        red = redC;
        blue = blueC;
        green = greenC;
        name = nameC;
    }

    public int getAlpha() {
        return alpha;
    }

    public int getRed() {
        return red;
    }

    public int getBlue() {
        return blue;
    }

    public int getGreen() {
        return green;
    }
}
