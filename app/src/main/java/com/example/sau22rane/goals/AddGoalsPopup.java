package com.example.sau22rane.goals;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

public class AddGoalsPopup extends AppCompatActivity {

    Button Add_Goal;
    EditText title, Description;
    TextView date;
    Calendar calendar;
    private int year, month, day, hour, min;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goals_popup);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.8), (int)(height*0.5));
        title = (EditText) findViewById(R.id.GoalTitleAdd);
        Description = (EditText) findViewById(R.id.GoalDescriptionAdd);
        date = (TextView) findViewById(R.id.GoalDateAdd);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(AddGoalsPopup.this, android.R.style.Theme_Material_Dialog_NoActionBar,  mDateSetListener, year,month,day);
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


        Add_Goal = (Button) findViewById(R.id.AddGoal);
        Add_Goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().contains("\'")){
                    title.setError("Title must not contain semicolon!");
                }
                else if( TextUtils.isEmpty(date.getText()) || TextUtils.isEmpty(Description.getText()) || TextUtils.isEmpty(title.getText())) {
                    if (TextUtils.isEmpty(date.getText())) {
                        date.setError("Date required!");
                    }
                    if (TextUtils.isEmpty(Description.getText())) {
                        Description.setError("Description required!");
                    }
                    if (TextUtils.isEmpty(title.getText())) {
                        title.setError("Title required!");
                    }
                }
                else {
                    SQLite a = new SQLite(getApplicationContext(), "DEFAULT", "Budgets", 1);
                    a.create_goal_table();
                    boolean result = a.add_goal(title.getText().toString(), Description.getText().toString(), date.getText().toString());
                    if (result) {
                        Toast.makeText(getApplicationContext(), "Data Added successfully", Toast.LENGTH_LONG).show();
                    }
                    finish();
                }
            }
        });
    }
}