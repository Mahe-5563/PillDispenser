package com.example.mahe.pilldispenser;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mahe.pilldispenser.ClassFiles.Tray;
import com.example.mahe.pilldispenser.TrayACtivity.ActivityTray1;
import com.example.mahe.pilldispenser.TrayACtivity.ActivityTray2;
import com.google.gson.Gson;


public class TraysActivity extends AppCompatActivity {

    ImageButton tray1, tray2;

    TextView tab1, tab2;


    SharedPreferences alarmclocksharedpreference;
    /*SharedPreferences.Editor alarmclockeditor;*/

    SharedPreferences tabletsharedpreference;
/*
    SharedPreferences.Editor tableteditor;


    final String SPALARM = "SPALARM";
*/

    final String SPNAF = "SPNAF";
/*

    final String SPTray1 = "SPTray1";
    final String DBNAME = "MYDB";
*/

    Tray t1 = new Tray();
    Tray t2 = new Tray();

    Button alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trays);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle("HOME");


        tray1 = findViewById(R.id.imgbtn_tab1);
        tray2 = findViewById(R.id.imgbtn_tab2);
        tab1 = findViewById(R.id.tv_tab1_name);
        tab2 = findViewById(R.id.tv_tab2_name);
        alarm = findViewById(R.id.btn_Alarm_set);


        alarmclocksharedpreference = getSharedPreferences(getString(R.string.SharedPreferencesDBNAME), MODE_PRIVATE);
        tabletsharedpreference = getSharedPreferences(getString(R.string.SharedPreferencesDBNAME), MODE_PRIVATE);

        //Checking for Alarms, if they are set.
        String spNaF = alarmclocksharedpreference.getString(SPNAF, null);


        //Checking for tablets, if they are set.
        Gson gson = new Gson();
        String json = tabletsharedpreference.getString(getString(R.string.tray1), "");
        String json2 = tabletsharedpreference.getString(getString(R.string.tray2), "");
        //Toast.makeText(this, json, Toast.LENGTH_SHORT).show();
        t1 = gson.fromJson(json, Tray.class);
        t2 = gson.fromJson(json2, Tray.class);


        if(t2 == null)
        {

            startActivity(new Intent(TraysActivity.this, ActivityTray2.class));

        }
        /*else
        {
            Toast.makeText(this, "TRAY 2:"+"\nMorning before food: "+t2.getMorn_bef_food()
                    +"\nMorning After Food: "+t2.getMorn_aft_food()
                    +"\nAfternoon Before Food: "+t2.getAftnoon_bef_food()
                    +"\nAfternoon After Food: "+t2.getAftnoon_aft_food()
                    +"\nNight Before Food: "+t2.getNight_bef_food()
                    +"\nNight After Food"+t2.getNight_aft_food(), Toast.LENGTH_LONG).show();
        }*/

        if(t1 == null)
        {

            startActivity(new Intent(TraysActivity.this, ActivityTray1.class));

        }
        /*else
        {
            Toast.makeText(this, "TRAY 1:"+"\nMorning before food: "+t1.getMorn_bef_food()
                    +"\nMorning After Food: "+t1.getMorn_aft_food()
                    +"\nAfternoon Before Food: "+t1.getAftnoon_bef_food()
                    +"\nAfternoon After Food: "+t1.getAftnoon_aft_food()
                    +"\nNight Before Food: "+t1.getNight_bef_food()
                    +"\nNight After Food"+t1.getNight_aft_food(), Toast.LENGTH_LONG).show();
        }
*/
        if(spNaF == null)
        {
            startActivity(new Intent(TraysActivity.this, AlarmActivity.class));
        }
        /*else
        {
            Toast.makeText(this, "TIME: "+"\nNight After Lunch: "+spNaF, Toast.LENGTH_SHORT).show();
        }*/


        tray1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(TraysActivity.this, ActivityTray1.class));

            }
        });

        tray2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(TraysActivity.this, ActivityTray2.class));

            }
        });

        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(TraysActivity.this, AlarmActivity.class));
            }
        });

    }
}
