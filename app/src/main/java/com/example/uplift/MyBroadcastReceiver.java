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
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class MyBroadcastReceiver extends BroadcastReceiver {

    public static final String NOTIFICATION_CHANNEL_ID = MyBroadcastReceiver.class.getSimpleName();
    public static final int NOTIFICATION_ID = 5453;
    RemoteViews notificationLayout;
    RemoteViews notificationLayoutExpanded;
    Context mContext;
    PendingIntent actionPendingIntent;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    ArrayList<String> categories;

    private String name;

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.e("ERROR", "In onReceive");
        categories = new ArrayList<>();

        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            mContext.startActivity(new Intent(mContext,SignInActivity.class));
        }
        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("categories/");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("ERROR", "In onDataChange");
                try {
                    for (DataSnapshot postSnapShot: dataSnapshot.getChildren()) {
                        String category = postSnapShot.getValue(String.class);
                        categories.add(category);
                        Log.e("ERROR", category);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sendNotification(context);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ERROR", "The read failed: " + databaseError.getCode());
            }
        });


    }

    private void sendNotification(Context context) {
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

        // determine category
        String randomCategory = getRandomCategory();

        Notification notification = isImageCategory(randomCategory) ? getImageNotification(randomCategory) : getTextNotification(randomCategory);
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

    public Notification getTextNotification(String category) {
        notificationLayout.setTextViewText(R.id.notification_text, category + "!");
        notificationLayoutExpanded.setTextViewText(R.id.notification_text_expanded, category + "!");

        String content = (String) getContent(category);
        notificationLayoutExpanded.setTextViewText(R.id.notification_text_main_expanded, content);

        NotificationCompat.Builder textNotification = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.small_icon)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayoutExpanded)
                .setContentIntent(actionPendingIntent);

        Log.e("ERROR", "Inside text notification");
        return textNotification.build();
    }
    public Notification getImageNotification(String category) {
        notificationLayout.setTextViewText(R.id.notification_text, "Open Me!");
        Integer content = (Integer) getContent(category);

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), content);
        notificationLayout.setImageViewBitmap(R.id.notification_image, bitmap);

        notificationLayoutExpanded.setImageViewBitmap(R.id.notification_image_expanded, bitmap);

        NotificationCompat.Builder imageNotification = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.small_icon)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayoutExpanded)
                .setContentIntent(actionPendingIntent);
        return imageNotification.build();
    }

    private String getRandomCategory() {
        Random random = new Random();
        int randomInt = random.nextInt(categories.size());
        Log.e("Category selected:", categories.get(randomInt));
        return categories.get(randomInt);
    }

    private Object getContent(String category) {
        ArrayList<Object> contentArray = Content.contentMap.get(category);
        Random random = new Random();
        Log.e("Content Array:", contentArray.toString());

        int randomInt = random.nextInt(contentArray.size());
        return contentArray.get(randomInt);
    }

    private boolean isImageCategory(String category) {
        return category.equals("Animals") || category.equals("Nature") || category.equals("Travel");
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