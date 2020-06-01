package com.example.sau22rane.goals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OptionsRitualsPopup extends AppCompatActivity {

    Button deleteRitual;
    FloatingActionButton fabEditRitual;
    String title,time,date;
    TextView option_title, option_time, option_date;
    RatingBar ratingBar;
    Float rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_rituals_popup);

        Intent intent = getIntent();
        if (null != intent) { //Null Checking
            Bundle b = intent.getExtras();
            if(b!=null) {
                rating = Float.parseFloat(b.getString("rating"));
                title = b.getString("title");
                time = b.getString("time");
                date = b.getString("date");
            }
        }
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.8), (int)(height*0.5));
        option_title = (TextView) findViewById(R.id.RitualTitleOptions);
        option_time = (TextView) findViewById(R.id.RitualTimeOptions);
        option_date = (TextView) findViewById(R.id.RitualDateOptions);
        option_date.setText(date);
        option_time.setText(time);
        option_title.setText(title);
        deleteRitual = (Button) findViewById(R.id.DeleteRitual);
        fabEditRitual = (FloatingActionButton) findViewById(R.id.fabEditRitual);
        ratingBar = (RatingBar) findViewById(R.id.OptionsRitualRating);
        ratingBar.setRating(rating);
        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ratingBar.setRating(rating);
                return true;
            }
        });


        fabEditRitual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EditRitualsPopup.class);
                Bundle bundle = new Bundle();
                bundle.putString("title",title);
                bundle.putString("time",time);
                bundle.putString("date",date);
                bundle.putString("rating",rating.toString());
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
        });

        deleteRitual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLite a = new SQLite(getApplicationContext(),"DEFAULT","BUDGET", 1);
                a.delete_row("RITUAL",title);
                finish();
            }
        });
    }
}