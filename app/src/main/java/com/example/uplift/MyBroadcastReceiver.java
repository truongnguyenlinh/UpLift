package com.example.uplift;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import java.io.File;

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

        RemoteViews notificationLayout = new RemoteViews("com.example.uplift",
                R.layout.notification_small);
        RemoteViews notificationLayoutExpanded = new RemoteViews("com.example.uplift",
                R.layout.notification_large);

        File root = Environment.getExternalStorageDirectory();
        Bitmap bMap = BitmapFactory.decodeFile("src/main/res/drawable/dog.jpg");

        Notification textNotification = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("Have you done outside today? Fresh air boosts your serotonin for a better mood!"))
//                .setCustomBigContentView(notificationLayoutExpanded)
                .build();

        Notification pictureNotification = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(bMap)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bMap)
                        .bigLargeIcon(null))
//                .setCustomBigContentView(notificationLayoutExpanded)
                .build();


//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
//                .setSmallIcon(R.drawable.uplift_blue)
//                .setColor(context.getColor(R.color.upliftBlue))
////                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
////                        R.mipmap.ic_launcher_round))
//                .setContentTitle("UpLift")
//                .setContentText("Have you done outside today? Fresh air boosts your serotonin for a better mood!")
//                .setVibrate(new long[] {0, 1000})
//                .setContentIntent(actionPendingIntent);

        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_ID+"_name",
                NotificationManager.IMPORTANCE_HIGH);

        NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.createNotificationChannel(channel);

        notifManager.notify(NOTIFICATION_ID, pictureNotification);
    }

}