package com.example.noman.snakeandladders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    /**
     *  onclick function which starts Gameplay activity
     */
    protected void start_the_game() {
        Intent intent = new Intent(this, Gameplay.class);
        startActivityForResult(intent,1);
    }

    /**
     *     function used to detect the press of the back button and it updates the user data.
     *     example : when user wins/computer the game the other activity calls finish() this function updates the stats.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
          update_stats();
        }
    }

    /**
     * Update the stats like wins,loss, level, points in the activity
     */
    public void update_stats() {


        SharedPreferences mPrefs = getSharedPreferences("label", 0);

        //print level
        TextView butt = (TextView) findViewById(R.id.leveltextview);
        butt.setText("Level:" + mPrefs.getInt("level",1));

        //Print points on the text view
        butt = (TextView) findViewById(R.id.pointstextview);
        butt.setText("Points:" + mPrefs.getInt("points", 0));

        //Print wins loss
        String text = "WINS:" + mPrefs.getInt("win", 0) + " LOSS:" + mPrefs.getInt("loss", 0);
        butt = (TextView) findViewById(R.id.winloss);
        butt.setText(text);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        //button onclick listener is set
        Button start_button = (Button) findViewById(R.id.start);
        start_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                start_the_game();
            }
        });


        update_stats();

    }


}


