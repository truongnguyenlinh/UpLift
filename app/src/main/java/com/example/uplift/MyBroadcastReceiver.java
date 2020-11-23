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
import android.widget.RemoteViews;

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

        notifManager.notify(NOTIFICATION_ID, getImageNotification());
    }

    public Notification getTextNotification() {
        Notification textNotification = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("Have you done outside today? Fresh air boosts your serotonin for a better mood!"))
//                .setCustomBigContentView(notificationLayoutExpanded)
                .setContentIntent(actionPendingIntent)
                .build();

        return textNotification;
    }

    public Notification getImageNotification() {

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.dog);

        Notification imageNotification = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(bitmap)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                        .bigLargeIcon(null))
//                .setCustomBigContentView(notificationLayoutExpanded)
                .build();

        return imageNotification;

    }

}