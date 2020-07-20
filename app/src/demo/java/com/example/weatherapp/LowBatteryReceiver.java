package com.example.weatherapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class LowBatteryReceiver extends BroadcastReceiver {
    private static final String NAME_MSG = "MSG";
    private static final String TAG = "LowBatteryReceiver";
    private int messageId=0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Low Battery", Toast.LENGTH_LONG).show();
        // Получить параметр сообщения
        String message = intent.getStringExtra(NAME_MSG);
        if (message == null) {
            message = "";
        }
        Log.d(TAG, message);
        // создать нотификацию
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "2")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Low Battery Receiver")
                .setContentText(message);

        PendingIntent contentIntentBattery = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntentBattery);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId++, builder.build());
    }
}

