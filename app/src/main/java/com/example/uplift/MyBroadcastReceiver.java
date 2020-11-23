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
import android.os.Vibrator;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class MyBroadcastReceiver extends BroadcastReceiver {
    public static final String NOTIFICATION_CHANNEL_ID = MyBroadcastReceiver.class.getSimpleName();
    public static final int NOTIFICATION_ID = 5453;
    RemoteViews notificationLayout;
    RemoteViews notificationLayoutExpanded;
    Context mContext;
    PendingIntent actionPendingIntent;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("ERROR", "In onReceive");
        Intent actionIntent = new Intent(context, MainActivity.class);
        mContext = context;

        actionPendingIntent = PendingIntent.getActivity(
                context,
                0,
                actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationLayout = new RemoteViews("com.example.uplift",
                R.layout.notification_small);
        notificationLayoutExpanded = new RemoteViews("com.example.uplift",
                R.layout.notification_large);

        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_ID+"_name",
                NotificationManager.IMPORTANCE_HIGH);

        NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.createNotificationChannel(channel);
        Notification notification = getTextNotification();
        notifManager.notify(NOTIFICATION_ID, notification);
        Log.e("ERROR", "Notification sent");
    }

    public Notification getNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("UpLift")
        .setContentText("Have you gone outside today? Fresh air boosts your serotonin for a better mood!")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(new long[] {0, 1000})
        .setContentIntent(actionPendingIntent)
        .setTimeoutAfter(300000)
        .setOngoing(true)
        .setAutoCancel(false);
        return builder.build();
    }

    public Notification getTextNotification() {
        NotificationCompat.Builder textNotification = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.small_icon)
                .setContentTitle("Uplift Category!")
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
//                .setCustomContentView(notificationLayout)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Have you gone outside today? Fresh air boosts your serotonin for a better mood!"))
//                .setCustomBigContentView(notificationLayoutExpanded)
                .setContentIntent(actionPendingIntent);

        Log.e("ERROR", "Inside text notification");
        return textNotification.build();
    }
    public Notification getImageNotification() {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.dog);
        Notification imageNotification = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.uplift_logo)
                .setLargeIcon(bitmap)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .setBigContentTitle("Category")
                        .bigPicture(bitmap)
                        .bigLargeIcon(null))
//                .setCustomBigContentView(notificationLayoutExpanded)
                .build();
        return imageNotification;
    }
}
//public class MyBroadcastReceiver extends BroadcastReceiver {
//    public static final String NOTIFICATION_CHANNEL_ID = MyBroadcastReceiver.class.getSimpleName();
//    public static final int NOTIFICATION_ID = 5453;
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Intent actionIntent = new Intent(context, MainActivity.class);
//
//        PendingIntent actionPendingIntent = PendingIntent.getActivity(
//                context,
//                0,
//                actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("UpLift")
//                .setContentText("Have you gone outside today? Fresh air boosts your serotonin for a better mood!")
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setVibrate(new long[] {0, 1000})
//                .setContentIntent(actionPendingIntent)
//                .setTimeoutAfter(300000)
//                .setOngoing(true)
//                .setAutoCancel(false);
//
//
//        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
//                NOTIFICATION_CHANNEL_ID+"_name",
//                NotificationManager.IMPORTANCE_HIGH);
//
//        NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notifManager.createNotificationChannel(channel);
//        Log.e("ERROR", "Sending notification");
//        notifManager.notify(NOTIFICATION_ID, builder.build());
//    }
//}