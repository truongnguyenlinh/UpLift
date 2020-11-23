package com.example.uplift;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class MyBroadcastReceiver extends BroadcastReceiver {
    public static final String NOTIFICATION_CHANNEL_ID = MyBroadcastReceiver.class.getSimpleName();
    public static final int NOTIFICATION_ID = 5453;
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent actionIntent = new Intent(context, MainActivity.class);

        PendingIntent actionPendingIntent = PendingIntent.getActivity(
                context,
                0,
                actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("UpLift")
                .setContentText("Have you gone outside today? Fresh air boosts your serotonin for a better mood!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[] {0, 1000})
                .setContentIntent(actionPendingIntent)
                .setTimeoutAfter(300000)
                .setOngoing(true)
                .setAutoCancel(false);


        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_ID+"_name",
                NotificationManager.IMPORTANCE_HIGH);

        NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.createNotificationChannel(channel);
        Log.e("ERROR", "Sending notification");
        notifManager.notify(NOTIFICATION_ID, builder.build());
    }
}