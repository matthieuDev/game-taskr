package com.educationportal;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Random;

/**
 * Created by muppala on 23/5/15.
 */
public class Enemy {
    int nbColor = 5 ;
    int nbForm  = 3 ;

    private final int FIRST_STEPY_VALUE = 5 ;
    private final int NB_SUCCESS_BEFORE_ADD_BALL = 1 ;
    private final int NB_SUCCESS_BEFORE_ACCELERATION = 2 ;

    private final int TRIANGLE_FORM = 0 ;
    private final int RECTANGLE_FORM = 1 ;
    private final int CIRCLE_FORM = 2 ;

    private final int colors[] = {Color.BLUE , Color.RED , Color.YELLOW , Color.GREEN , Color.GRAY} ;
    Random rd = new Random();



    float x; // Guy's top left corner (x,y)
    float y;
    float radius = 30 ;
    float stepX = 10; // Guy's step in (x,y) direction
    float stepY = FIRST_STEPY_VALUE; // gives speed of motion, larger means faster speed
    int lowerX, lowerY, upperX, upperY; // boundaries
    Paint paint ;

    int enemyShotColor[] = new int[1] ;
    int enemyShotForm[] = new int[1] ;
    int form = rd.nextInt(nbForm) ;
    int indexEnemyShot = 0 ;


    private Context mContext;

    // Constructors
    public Enemy(Context c) {

        mContext = c;
        paint = new Paint();

    }
    public Enemy(int color, Context c) {

        mContext = c;
        paint = new Paint();
        paint.setColor(color);

    }
    public Enemy(int color, Context c, int x, int radius) {

        mContext = c;
        paint = new Paint();
        paint.setColor(color);
        this.x = x ;
        this.radius = radius ;
    }

    public void setNb ( int nbColor , int nbForm ) {
        this.nbColor = nbColor ;
        this.nbForm = nbForm ;
        paint.setColor(colors[rd.nextInt(nbColor)]);
        form = rd.nextInt(nbForm) ;
    }

    public void setNbLine ( int nbColor , int nbForm ) {
        this.nbColor = nbColor ;
        this.nbForm = nbForm ;
    }
    public void beginEnemy(int level ) {
        enemyShotColor = new int[level/NB_SUCCESS_BEFORE_ADD_BALL + 1] ;
        enemyShotForm = new int[level/NB_SUCCESS_BEFORE_ADD_BALL + 1] ;
        stepY = FIRST_STEPY_VALUE + level/NB_SUCCESS_BEFORE_ACCELERATION ; ;
    }
    public void setBounds(int lx, int ly, int ux, int uy) {
        lowerX = lx;
        lowerY = ly;
        upperX = ux;
        upperY = uy;

        x = (float) ( radius + (upperX-2*radius)*Math.random());
        y = 0;
    }

    public boolean move() {
        // Get new (x,y) position. Movement is always in vertical direction downwards
        y += stepY;
        // Detect when the guy reaches the bottom of the screen
        // restart at a random location at the top of the screen
        if (y + 50 > upperY) {
            x = (float) ((upperX-50)*Math.random());
            y = 0;
         //   SoundEffects.INSTANCE.playSound(SoundEffects.SOUND_GUY);
            indexEnemyShot = 0 ;
            return false;
        }
        else
            return true;
    }

    public boolean moveForLine() {
        // Get new (x,y) position. Movement is always in vertical direction downwards
        y += stepY;
        // Detect when the guy reaches the bottom of the screen
        // restart at location at the top of the screen
        if (y + 50 > upperY) {
///            SoundEffects.INSTANCE.playSound(SoundEffects.SOUND_GUY);
            return false;
        }
        else
            return true;
    }

    // When you reset, starts the Android Guy from a random X co-ordinate location
    // at the top of the screen again
    public void reset() {
        x = (float) ((upperX-50)*Math.random());
        y = 0;
        paint.setColor(colors[rd.nextInt(nbColor)]);
    }

    public void resetY() {
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

    public int getColor() { return paint.getColor();}

    public int getForm() { return form;}

    public void resetForm() { form = (form + 1) %nbForm ;}
    public void setForm() { form = 0 ;}

    public int[] getEnemyShotForm() { return enemyShotForm ;}

    public int[] getEnemyShotColor() { return enemyShotColor ;}

    public void draw(Canvas canvas) {
        if ( form == RECTANGLE_FORM ) {
            canvas.drawRect(x - radius , y - radius , x+radius  , y+radius  , paint );
        } else if ( form == CIRCLE_FORM ) {
            canvas.drawCircle(x, y, radius, paint);
        } else if ( form == TRIANGLE_FORM) {
            drawTriangle(canvas);
        }

    }

    public boolean hitAndIfNext() {
        enemyShotColor[indexEnemyShot] = getColor() ;
        enemyShotForm[indexEnemyShot] = form ;
        form = rd.nextInt(nbForm) ;
        indexEnemyShot ++ ;
        if ( indexEnemyShot == enemyShotColor.length ) {
            indexEnemyShot = 0 ;
            return true ;
        } else {
            return false ;
        }
    }

    public void drawTriangle ( Canvas canvas ) {
        float lines [] = { x - radius , y , x + radius , y ,
                x - radius , y , x , y + 1.73205f * radius ,
                x , y + 1.73205f * radius , x + radius/2 , y } ;
        canvas.drawLines(lines , paint );
        canvas.drawCircle(x + radius / 3 , y + radius / 3  , radius / 3 , paint);
    }
}
