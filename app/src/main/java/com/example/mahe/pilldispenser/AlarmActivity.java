package com.example.mahe.pilldispenser;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mahe.pilldispenser.ClassFiles.AlertReciever;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {

    Button btn_save;
    TextView mbf, maf, abf, aaf, nbf, naf;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String time = null;

    final String SPALARM = "SPALARM";

    final String SPMBF = "SPMBF";
    final String SPMAF = "SPMAF";
    final String SPABF = "SPABF";
    final String SPAAF = "SPAAF";
    final String SPNBF = "SPNBF";
    final String SPNAF = "SPNAF";

    String spMbF;
    String spMaF;
    String spAbF;
    String spAaF;
    String spNbF;
    String spNaF;

    Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar()!=null)
        getSupportActionBar().setTitle("Choose Timings.");

        setContentView(R.layout.activity_alarm);

        sharedPreferences = getSharedPreferences(getString(R.string.SharedPreferencesDBNAME), MODE_PRIVATE);
        editor = sharedPreferences.edit();


        cal = Calendar.getInstance();

        mbf = findViewById(R.id.tv_time_mbf);
        maf = findViewById(R.id.tv_time_maf);
        abf = findViewById(R.id.tv_time_abf);
        aaf = findViewById(R.id.tv_time_aaf);
        nbf = findViewById(R.id.tv_time_nbf);
        naf = findViewById(R.id.tv_time_naf);
        btn_save = findViewById(R.id.btn_saveChanges);

        spMbF = sharedPreferences.getString(SPMBF, null);
        spMaF = sharedPreferences.getString(SPMAF, null);
        spAbF = sharedPreferences.getString(SPABF, null);
        spAaF = sharedPreferences.getString(SPAAF, null);
        spNbF = sharedPreferences.getString(SPNBF, null);
        spNaF = sharedPreferences.getString(SPNAF, null);

        if(spMbF == null || spMaF == null || spAbF == null ||
                spAaF == null || spNbF == null || spNaF == null)
        {
            Toast.makeText(this, "Set all your Alarm Times.", Toast.LENGTH_SHORT).show();
        }

        else
        {
            setTexts(); // If the preferences have some value, then display those values.
        }

        mbf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setTimeFunc(mbf, SPMBF);

            }
        });

        maf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setTimeFunc(maf, SPMAF);

            }
        });

        abf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setTimeFunc(abf, SPABF);

            }
        });

        aaf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setTimeFunc(aaf, SPAAF);

            }
        });

        nbf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setTimeFunc(nbf, SPNBF);

            }
        });

        naf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setTimeFunc(naf, SPNAF);

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               int a = checkValues();

               if( a == 1 )
               {

                   Toast.makeText(AlarmActivity.this, "Alarms Set", Toast.LENGTH_LONG).show();
                  /// startActivity(new Intent(AlarmActivity.this, HomeActivity.class));
                   finish();

               }

            }
        });

    }

    private int checkValues() {

        /*String spMbF = sharedPreferences.getString(SPMBF, null);
        String spMaF = sharedPreferences.getString(SPMAF, null);
        String spAbF = sharedPreferences.getString(SPABF, null);
        String spAaF = sharedPreferences.getString(SPAAF, null);
        String spNbF = sharedPreferences.getString(SPNBF, null);
        String spNaF = sharedPreferences.getString(SPNAF, null);*/

        if(spMbF == null || spMaF == null || spAbF == null ||
                spAaF == null || spNbF == null || spNaF == null)
        {

            Toast.makeText(this, "Please Set time for all the Alarms.", Toast.LENGTH_SHORT).show();

            return 0;

        }

        else
        {

            setTexts(); // If the preferences have some value, then display those values.

            setAlarms(); // IF you wish to set the alarm, set it

            return 1;
        }
    }

    private void setTexts() {

        mbf.setText(spMbF);
        maf.setText(spMaF);
        abf.setText(spAbF);
        aaf.setText(spAaF);
        nbf.setText(spNbF);
        naf.setText(spNaF);

    }

    private void setAlarms()
    {

        String spmbf = sharedPreferences.getString(SPMBF, null);

        callAlarm(spmbf,  1);

        String spMaF = sharedPreferences.getString(SPMAF, null);

        callAlarm(spMaF,  2);

        String spAbF = sharedPreferences.getString(SPABF, null);

        callAlarm(spAbF,  3);

        String spAaF = sharedPreferences.getString(SPAAF, null);

        callAlarm(spAaF,  4);

        String spNbF = sharedPreferences.getString(SPNBF, null);

        callAlarm(spNbF,  5);

        String spNaF = sharedPreferences.getString(SPNAF, null);

        callAlarm(spNaF,  6);

    }

    private void callAlarm(String sp, int i)
    {
        String arr[] = sp.split(":");
        String hour = arr[0];
        String min =  arr[1];

        Integer h = Integer.valueOf(hour);
        Integer m = Integer.valueOf(min);

        cal.set(Calendar.HOUR_OF_DAY, h);
        cal.set(Calendar.MINUTE, m);
        cal.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Log.d("Pill Despenser", "Entered set Alarm");
        Intent inty = new Intent(this, AlertReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, inty, 0);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.d("Pill Dispenser", "alarm   manager completed");
            //Toast.makeText(this, "Alarm Set!!", Toast.LENGTH_SHORT).show();
        }

    }


    private void setTimeFunc(final TextView tex, final String SP)
    {

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(AlarmActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                time = selectedHour + ":" + selectedMinute;

                editor.putString(SP, time);
                editor.apply();

                tex.setText(time);

            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }


}
