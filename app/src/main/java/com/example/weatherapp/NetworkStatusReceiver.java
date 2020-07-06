package com.example.weatherapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class NetworkStatusReceiver extends BroadcastReceiver {
    private static final String NAME_MSG = "MSG";
    private static final String TAG = "NetworkStatusReceiver";
    private int messageId=0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Network Status", Toast.LENGTH_LONG).show();
        // Получить параметр сообщения
        String message = intent.getStringExtra(NAME_MSG);
        if (message == null) {
            message = "";
        }
        Log.d(TAG, message);
        // создать нотификацию
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "2")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Network Status Receiver")
                .setContentText(message);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId++, builder.build());
    }
}



