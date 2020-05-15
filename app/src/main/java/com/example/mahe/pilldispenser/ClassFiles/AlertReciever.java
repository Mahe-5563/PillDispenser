package com.example.mahe.pilldispenser.ClassFiles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.mahe.pilldispenser.NotificationReciever;

public class AlertReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("Pill Dispenser", "Entered Alert Reciever");
        //String state = intent.getExtras().getString("extra");
        //Log.e("MyActivity", "In the receiver with " + state);

        Intent serviceIntent = new Intent(context,NotificationReciever.class);
        //serviceIntent.putExtra("extra", state);

        context.startService(serviceIntent);


    }
}
