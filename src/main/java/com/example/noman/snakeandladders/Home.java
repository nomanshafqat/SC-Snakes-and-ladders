package com.example.noman.snakeandladders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.res.Resources;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Home extends AppCompatActivity {
    private Resources resources;
    //a string to output the contents of the files to LogCat


    protected void start_the_game() {
        Intent intent = new Intent(this, Gameplay.class);
        startActivityForResult(intent,1);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
          intial_setter();
        }
    }
    public void intial_setter() {


        SharedPreferences mPrefs = getSharedPreferences("label", 0);

        TextView butt = (TextView) findViewById(R.id.leveltextview);
        butt.setText("Level:" + mPrefs.getInt("level",1));


        butt = (TextView) findViewById(R.id.pointstextview);
        butt.setText("Points:" + mPrefs.getInt("points", 0));

        String text = "WINS:" + mPrefs.getInt("win", 0) + " LOSS:" + mPrefs.getInt("loss", 0);
        butt = (TextView) findViewById(R.id.winloss);
        butt.setText(text);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Button start_button = (Button) findViewById(R.id.start);
        start_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                start_the_game();
            }
        });
        intial_setter();
    }


}


