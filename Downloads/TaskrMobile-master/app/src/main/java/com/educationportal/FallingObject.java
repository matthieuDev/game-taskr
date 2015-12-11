package com.educationportal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by muppala on 23/5/15.
 */
public class FallingObject {
    float x; // Guy's top left corner (x,y)
    float y;
    float stepX = 10; // Guy's step in (x,y) direction
    float stepY = 5; // gives speed of motion, larger means faster speed
    int lowerX, lowerY, upperX, upperY; // boundaries

    int radius = 50;

    int mode;
    int type;

    int[] colors = {Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN};

    String[] colorText = {"red", "blue", "yellow", "green"};

    Bitmap android_guy;


    private Context mContext;

    private Paint paint; // The paint style, color used for drawing

    // Constructor
    public FallingObject(int mode, int type, Context c) {

        paint = new Paint();
        paint.setColor(colors[type]);

        System.out.println(" - > " + colorText[type]);

        mContext = c;
    }

    public void setBounds(int lx, int ly, int ux, int uy) {
        lowerX = lx;
        lowerY = ly;
        upperX = ux;
        upperY = uy;

        x = (float) ((upperX-radius)*Math.random());
        y = 0;
    }

    public boolean move() {
        // Get new (x,y) position. Movement is always in vertical direction downwards
        y += stepY;
        // Detect when the guy reaches the bottom of the screen
        // restart at a random location at the top of the screen
        if (y + radius > upperY) {
            x = (float) ((upperX-radius)*Math.random());
            y = 0;
            return true;
        }
        else
            return true;
    }

    // When you reset, starts the Android Guy from a random X co-ordinate location
    // at the top of the screen again
    public void reset(int mode, int type) {
        paint.setColor(colors[type]);
        x = (float) ((upperX-radius)*Math.random());
        y = 0;
    }

    // Returns the rectangle enclosing the Guy. Used for collision detection
    public RectF getRect() {
        return new RectF(x-radius,y-radius,x+radius,y+radius);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(x, y, radius, paint);
    }
}
