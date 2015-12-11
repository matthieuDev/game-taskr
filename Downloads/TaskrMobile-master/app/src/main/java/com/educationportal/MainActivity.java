package com.educationportal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.firebase.client.Firebase;

public class MainActivity extends AppCompatActivity {
    ListView l1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);
    }

    public void openTasks(View view) {
        Intent intent = new Intent(this, Quests.class);
        startActivityForResult(intent, 1);
    }

    public void openGame(View view) {
        Intent intent = new Intent(this, ShootingGame.class);
        startActivityForResult(intent, 2);
    }


    public void openGame2(View view) {
        Intent intent = new Intent(this, ShootingGame2.class);
        startActivityForResult(intent, 3);
    }
}
