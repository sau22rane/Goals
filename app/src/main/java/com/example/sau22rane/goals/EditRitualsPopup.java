package com.example.sau22rane.goals;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class EditRitualsPopup extends AppCompatActivity {

    Button Save_Ritual;
    EditText title;
    TextView date, timehr, timemin;
    String time;
    String datestr,timestr,titlestr;
    Calendar calendar;
    RatingBar ratingBar;
    String[] timedata;
    Float rating;
    private int year, month, day, hour, min;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rituals_popup);

        Intent intent = getIntent();
        if (null != intent) { //Null Checking
            Bundle b = intent.getExtras();
            if(b!=null) {
                rating = Float.parseFloat(b.getString("rating"));
                titlestr = b.getString("title");
                timestr = b.getString("time");
                timedata = timestr.split(":");
                datestr = b.getString("date");
            }
        }

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.8), (int)(height*0.5));
        title = (EditText) findViewById(R.id.RitualTitleEdit);
        timehr = (TextView) findViewById(R.id.RitualTimeEdithr);;
        timemin = (TextView) findViewById(R.id.RitualTimeEditmin);
        date = (TextView) findViewById(R.id.RitualDateEdit);
        ratingBar = (RatingBar) findViewById(R.id.EditRitualRating);
        ratingBar.setRating(rating);
        title.setText(titlestr);
        timehr.setText(timedata[0]);
        timemin.setText(timedata[1]);
        date.setText(datestr);


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(EditRitualsPopup.this, android.R.style.Theme_Material_Dialog_NoActionBar,  mDateSetListener, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(41,65,67)));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d("TAG", "onDateSet: mm/dd/yyy: " + day + "/" + month + "/" + year);

                String str = day + "/" + month + "/" + year;
                date.setText(str);
            }
        };


        Save_Ritual = (Button) findViewById(R.id.SaveRitual);
        Save_Ritual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().contains("\'")){
                    title.setError("Title must not contain semicolon!");
                }
                else if( TextUtils.isEmpty(date.getText()) || (TextUtils.isEmpty(timehr.getText())&&TextUtils.isEmpty(timemin.getText())) || TextUtils.isEmpty(title.getText())) {
                    if (TextUtils.isEmpty(date.getText())) {
                        date.setError("Date required!");
                    }
                    if (TextUtils.isEmpty(timehr.getText())&&TextUtils.isEmpty(timemin.getText())) {
                        timemin.setError("Time required!");
                    }
                    if (TextUtils.isEmpty(title.getText())) {
                        title.setError("Title required!");
                    }
                }
                else if(!(Integer.parseInt(timehr.getText().toString())<=24 && Integer.parseInt(timehr.getText().toString())>=0 && Integer.parseInt(timemin.getText().toString())<=59 && Integer.parseInt(timemin.getText().toString())>=0)){
                    timemin.setError("Invalid time");
                }
                else {
                    rating = ratingBar.getRating();
                    time = timehr.getText().toString()+":"+timemin.getText().toString();
                    SQLite a = new SQLite(getApplicationContext(), "DEFAULT", "Budgets", 1);
                    a.update("RITUAL",titlestr,title.getText().toString(), time, date.getText().toString(), rating.toString());
                    finish();
                }
            }
        });
    }
}