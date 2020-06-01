package com.example.sau22rane.goals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OptionsExpectationsPopup extends AppCompatActivity {

    Button deleteExpectation;
    FloatingActionButton fabEditExpectation;
    String title,time,date;
    TextView option_title, option_time, option_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_expectations_popup);

        Intent intent = getIntent();
        if (null != intent) { //Null Checking
            Bundle b = intent.getExtras();
            if(b!=null) {
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
        option_title = (TextView) findViewById(R.id.ExpectationTitleOptions);
        option_time = (TextView) findViewById(R.id.ExpectationTimeOptions);
        option_date = (TextView) findViewById(R.id.ExpectationDateOptions);
        option_date.setText(date);
        option_time.setText(time);
        option_title.setText(title);
        deleteExpectation = (Button) findViewById(R.id.DeleteExpectations);
        fabEditExpectation = (FloatingActionButton) findViewById(R.id.fabEditExpectations);


        fabEditExpectation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EditExpectationsPopup.class);
                Bundle bundle = new Bundle();
                bundle.putString("title",title);
                bundle.putString("time",time);
                bundle.putString("date",date);
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
        });

        deleteExpectation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLite a = new SQLite(getApplicationContext(),"DEFAULT","BUDGET", 1);
                a.delete_row("EXPECTATION",title);
                finish();
            }
        });
    }
}