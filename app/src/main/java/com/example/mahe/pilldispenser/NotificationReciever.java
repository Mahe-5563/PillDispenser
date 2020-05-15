package com.example.mahe.pilldispenser;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.example.mahe.pilldispenser.ClassFiles.Tray;
import com.google.gson.Gson;

import java.util.Calendar;

public class NotificationReciever extends Service {

    SharedPreferences sharedPreferences;

    Calendar currTime;

    final String SPMBF = "SPMBF";
    final String SPMAF = "SPMAF";
    final String SPABF = "SPABF";
    final String SPAAF = "SPAAF";
    final String SPNBF = "SPNBF";
    final String SPNAF = "SPNAF";

    PendingIntent pIntent;

    //p-dMediaPlayer mediaPlayer;

    NotificationManager mNM;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.d("Pill Dispenser", "Inside Notification Helper");

        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sharedPreferences = getSharedPreferences(getString(R.string.SharedPreferencesDBNAME), MODE_PRIVATE);
        currTime = Calendar.getInstance();


        String spMbF = sharedPreferences.getString(SPMBF, null);
        String spMaF = sharedPreferences.getString(SPMAF, null);
        String spAbF = sharedPreferences.getString(SPABF, null);
        String spAaF = sharedPreferences.getString(SPAAF, null);
        String spNbF = sharedPreferences.getString(SPNBF, null);
        String spNaF = sharedPreferences.getString(SPNAF, null);


        String jsont1 = sharedPreferences.getString(getString(R.string.tray1), null);
        String jsont2 = sharedPreferences.getString(getString(R.string.tray2), null);

        Toast.makeText(this, jsont1, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, jsont2, Toast.LENGTH_SHORT).show();

        Gson gson = new Gson();
        Tray tray1 = gson.fromJson(jsont1, Tray.class);
        Tray tray2 = gson.fromJson(jsont2, Tray.class);


        //get the current time and see under what time it comes under
        //and compare if the tablet should be taken

        int hour = currTime.get(Calendar.HOUR_OF_DAY);
        int min = currTime.get(Calendar.MINUTE);

        String currenttimeinString = hour + ":" + min;


        int ID = -1;

        if (currenttimeinString.equals(spMbF)) {
            ID = 1;

        } else if (currenttimeinString.equals(spMaF)) {
            ID = 2;
        } else if (currenttimeinString.equals(spAbF)) {
            ID = 3;
        } else if (currenttimeinString.equals(spAaF)) {
            ID = 4;
        } else if (currenttimeinString.equals(spNbF)) {
            ID = 5;
        } else if (currenttimeinString.equals(spNaF)) {
            ID = 6;
        }




        switch (ID) {
            case 1:
                if (tray1.getMorn_bef_food() == 1) {

                    NotifyMe("Take Your Morning Pill", "Before Breakfast", 1);

                }
                break;


            case 2:
                if (tray1.getMorn_aft_food() == 1) {

                    NotifyMe("Take Your Morning Pill", "After Breakfast", 2);

                }
                break;
            case 3:
                if (tray1.getAftnoon_bef_food() == 1) {

                    NotifyMe("Take Your Afternoon Pill", "Before Lunch", 3);

                }
                break;


            case 4:
                if (tray1.getAftnoon_aft_food() == 1) {

                    NotifyMe("Take Your Afternoon Pill", "After Lunch", 4);

                }
                break;
            case 5:
                if (tray1.getNight_bef_food() == 1) {


                    NotifyMe("Take Your Dinner Pill", "Before Dinner", 5);

                }
                break;


            case 6:
                if (tray1.getNight_aft_food() == 1) {


                    NotifyMe("Take Your Dinner Pill", "After Dinner", 6);

                }
                break;
        }


        return super.onStartCommand(intent, flags, startId);
    }


    public void NotifyMe(String Title, String Message, int id)
    {

        mNM = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        Intent intent1 = new Intent(this.getApplicationContext(), Bluetooth.class);

        //Toast.makeText(this, "Value:" + id, Toast.LENGTH_SHORT).show();

        intent1.putExtra(getString(R.string.intenty), id);

        pIntent = PendingIntent.getActivity(this, id, intent1, 0);

        Notification mNotify = new Notification.Builder(this)
                .setContentTitle(Title)
                .setContentText(Message)
                .setSmallIcon(R.drawable.pilldispenserlogo)
                .setContentIntent(pIntent)
                .setAutoCancel(false)
                .build();

        /*mediaPlayer = MediaPlayer.create(this, R.raw.richard_dawkins_1);

        mediaPlayer.start();
*/
        mNM.notify(id, mNotify);


    }


    @Override
    public void onDestroy() {
        Log.e("Pill Dispenser", "on destroy called");
        super.onDestroy();
    }

}
