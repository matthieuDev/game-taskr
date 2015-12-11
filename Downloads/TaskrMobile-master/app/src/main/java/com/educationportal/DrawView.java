package com.educationportal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by muppala on 23/5/15.
 */
public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    private int width, height;
    private DrawViewThread drawviewthread;

    private int type;

    private int numOfModes = 2;

    private String[] colors = {"red", "blue", "yellow", "green"};

    // mode determines if shapes or colors are falling
    // 0: colors
    // 1: shapes
    private int mode = 0;

    ArrayList<Integer> previousItems = new ArrayList<Integer>();

    Context mContext;

    // We can have multiple bullets and explosions
    // keep track of them in ArrayList
    ArrayList<Bullet> bullets;
    ArrayList<Explosion> explosions;
    Cannon cannon;
    FallingObject fallingObject;
    Score score;


    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        getHolder().addCallback(this);

        setFocusable(true);
        this.requestFocus();

        // create a cannon object
        cannon = new Cannon(Color.BLUE,mContext);

        // create arraylists to keep track of bullets and explosions
        bullets = new ArrayList<Bullet> ();
        explosions = new ArrayList<Explosion>();

        type = 0;

        generateFallingObject();
        previousItems.add(type);

        score = new Score(Color.BLACK);

    }

    public void generateFallingObject() {
        type = (int)Math.floor(Math.random() * 4);
        fallingObject = new FallingObject(mode, type, mContext);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        drawviewthread = new DrawViewThread(holder);
        drawviewthread.setRunning(true);
        drawviewthread.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        boolean retry = true;
        drawviewthread.setRunning(false);

        while (retry) {
            try {
                drawviewthread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }

    }

    public class DrawViewThread extends Thread{
        private SurfaceHolder surfaceHolder;
        private boolean threadIsRunning = true;

        public DrawViewThread(SurfaceHolder holder){
            surfaceHolder = holder;
            setName("DrawViewThread");
        }

        public void setRunning (boolean running){
            System.out.println(running);
            threadIsRunning = running;
        }

        public void run() {
            Canvas canvas = null;

            while (true) {
                if(threadIsRunning) {
                    try {
                        canvas = surfaceHolder.lockCanvas(null);

                        synchronized(surfaceHolder){
                            drawGameBoard(canvas);
                        }
                        sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finally {
                        if (canvas != null)
                            surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;

        cannon.setBounds(0,0,width, height);
        fallingObject.setBounds(0, 0, width, height);
        for (int i = 0; i < bullets.size(); i++ ) {
            bullets.get(i).setBounds(0,0,width,height);
        }

    }

    public void drawGameBoard(Canvas canvas) {
        canvas.drawColor(Color.WHITE);     //if you want another background color
        // Draw the cannon
        cannon.draw(canvas);

        // Draw all the bullets
        for (int i = 0; i < bullets.size(); i++ ) {
            if (bullets.get(i) != null) {
                bullets.get(i).draw(canvas);

                if (bullets.get(i).move() == false) {
                    bullets.remove(i);
                }
            }
        }

        // Draw all the explosions, at those locations where the bullet
        // hits the Android Guy
        for (int i = 0; i < explosions.size(); i++ ) {
            if (explosions.get(i) != null) {
                if (explosions.get(i).draw(canvas) == false) {
                    explosions.remove(i);
                }
            }
        }

        // If the Android Guy is falling, check to see if any of the bullets
        // hit the Guy
        if (fallingObject != null) {
            fallingObject.draw(canvas);

            RectF guyRect = fallingObject.getRect();

            for (int i = 0; i < bullets.size(); i++ ) {

                // The rectangle surrounding the Guy and Bullet intersect, then it's a collision
                // Generate an explosion at that location and delete the Guy and bullet. Generate
                // a new Android Guy to fall from the top.
                if (RectF.intersects(guyRect, bullets.get(i).getRect())) {
                    explosions.add(new Explosion(Color.RED, mContext, fallingObject.getX(), fallingObject.getY()));
                    type = (int)Math.floor(Math.random() * 4);
                    bullets.remove(i);

                    System.out.println(colors[type]);

                    fallingObject.reset(mode, type);

                    if(previousItems.size() >= 3) {
                        ((Activity)getContext()).runOnUiThread(new Runnable() {
                            public void run() {
                                //stopGame();
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext).setCancelable(false);

                                LayoutInflater inflater;

                                inflater = LayoutInflater.from(mContext);
                                builder.setView(inflater.inflate(R.layout.shooting_popup_layout, null));

                                final AlertDialog dialog = builder.create();

                                dialog.show();

                                dialog.setContentView(R.layout.shooting_popup_layout);

                                // handle user options
                                Button option1 = (Button) dialog.findViewById(R.id.option1);
                                Button option2 = (Button) dialog.findViewById(R.id.option2);
                                Button option3 = (Button) dialog.findViewById(R.id.option3);
                                Button option4 = (Button) dialog.findViewById(R.id.option4);

                                final int answer = generateOptions(option1, option2, option3, option4);

                                stopGame();

                                option1.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        option1(dialog, answer);
                                    }
                                });

                                option2.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        option2(dialog, answer);
                                    }
                                });

                                option3.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        option3(dialog, answer);
                                    }
                                });

                                option4.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        option4(dialog, answer);
                                    }
                                });
                            }

                        });
                    }

                    previousItems.add(type);

                    //ask for previous colors, shapes


                    // Play the explosion sound by calling the SoundEffects class
                    SoundEffects.INSTANCE.playSound(SoundEffects.SOUND_EXPLOSION);
                    score.incrementScore(10);
                    break;
                }

            }

            if (fallingObject.move() == false) {
                fallingObject = null;
            }
        }
        score.draw(canvas);

    }

    public int generateOptions(Button o1, Button o2, Button o3, Button o4) {
        Button[] btns = {o1, o2, o3, o4};
        for(int i = 0; i < 4; i++) {

            String txt = "";

            for (int j = 0; j < 3; j++) {
                String color = colors[(int)Math.floor(Math.random() * 4)];
                txt = txt + color + " ";
            }

            btns[i].setText(txt);
        }

        int answer = (int)Math.floor(Math.random() * 4);

        String answerTxt = "";

        for(int i = 0; i < 3; i++) {
            answerTxt = answerTxt + colors[previousItems.get(i)];
        }

        btns[answer].setText(answerTxt);

        previousItems = new ArrayList<Integer>();

        return answer;
    }

    public void option1(AlertDialog d, int answer) {
        if(answer == 0) {
            score.incrementScore(100);
        };
        d.dismiss();
        resumeGame();
    }

    public void option2(AlertDialog d, int answer) {
        if(answer == 1) {
            score.incrementScore(100);
        };
        d.dismiss();
        resumeGame();
    }

    public void option3(AlertDialog d, int answer) {
        if(answer == 2) {
            score.incrementScore(100);
        };
        d.dismiss();
        resumeGame();
    }

    public void option4(AlertDialog d, int answer) {
        if(answer == 3) {
            score.incrementScore(100);
        };
        d.dismiss();
        resumeGame();
    }

    // Move the cannon left or right
    public void moveCannonLeft() {
        cannon.moveLeft();
    }

    public void moveCannonRight() {
        cannon.moveRight();
    }

    // Whenever the user shoots a bullet, create a new bullet moving upwards
    public void shootCannon() {

        bullets.add(new Bullet(Color.RED, mContext, cannon.getPosition(), (float) (height - 40)));

    }

    public void stopGame(){
        if (drawviewthread != null){
            drawviewthread.setRunning(false);
        }
    }

    public void resumeGame(){
        System.out.println(drawviewthread);
        if (drawviewthread != null){
            drawviewthread.setRunning(true);
        }
    }

    public void releaseResources(){

    }

}
