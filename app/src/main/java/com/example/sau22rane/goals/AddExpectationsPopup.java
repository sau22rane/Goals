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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddExpectationsPopup extends AppCompatActivity {

    Button Add_Expectation;
    EditText title;
    TextView date, time;
    Calendar calendar;
    private int year, month, day, hour, min;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    String goal;

    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expectations_popup);

        Intent intent = getIntent();
        if (null != intent) { //Null Checking
            Bundle b = intent.getExtras();
            if(b!=null) {
                goal = b.getString("goal");
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
        title = (EditText) findViewById(R.id.ExpectationTitleAdd);
        time = (TextView) findViewById(R.id.ExpectationTimeAdd);
        date = (TextView) findViewById(R.id.ExpectationDateAdd);

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(AddExpectationsPopup.this,android.R.style.Theme_Material_Dialog_NoActionBar, mTimeSetListener, hour,min,true );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(41,65,67)));
                dialog.show();
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(AddExpectationsPopup.this, android.R.style.Theme_Material_Dialog_NoActionBar,  mDateSetListener, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(41,65,67)));
                dialog.show();
            }
        });
        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String str = hourOfDay+":"+minute;
                time.setText(str);
            }
        };
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d("TAG", "onDateSet: mm/dd/yyy: " + day + "/" + month + "/" + year);

                String str = day + "/" + month + "/" + year;
                date.setText(str);
            }
        };


        Add_Expectation = (Button) findViewById(R.id.AddExpectations);
        Add_Expectation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().contains("\'")){
                    title.setError("Title must not contain semicolon!");
                }
                else if( TextUtils.isEmpty(date.getText()) || TextUtils.isEmpty(time.getText()) || TextUtils.isEmpty(title.getText())) {
                    if (TextUtils.isEmpty(date.getText())) {
                        date.setError("Date required!");
                    }
                    if (TextUtils.isEmpty(time.getText())) {
                        time.setError("Time required!");
                    }
                    if (TextUtils.isEmpty(title.getText())) {
                        title.setError("Title required!");
                    }
                }
                else {
                    SQLite a = new SQLite(getApplicationContext(), "DEFAULT", "Budgets", 1);
                    boolean result = a.add_expectation(title.getText().toString(), time.getText().toString(), date.getText().toString(),goal);
                    if (result) {
                        Toast.makeText(getApplicationContext(), "Data Added successfully", Toast.LENGTH_LONG).show();
                    }
                    finish();
                }
            }
        });
    }
}