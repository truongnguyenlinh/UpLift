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
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
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

public class MyBroadcastReceiver extends BroadcastReceiver {

    public static final String NOTIFICATION_CHANNEL_ID = MyBroadcastReceiver.class.getSimpleName();
    public static final int NOTIFICATION_ID = 5453;
    RemoteViews notificationLayout;
    RemoteViews notificationLayoutExpanded;
    Context mContext;
    Intent actionIntent;
    PendingIntent actionPendingIntent;

    ArrayList<String> categories;

    @Override
    public void onReceive(final Context context, Intent intent) {
        categories = new ArrayList<>();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            mContext.startActivity(new Intent(mContext,SignInActivity.class));
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("categories/");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ERROR", "The read failed: " + databaseError.getCode());
            }
        });
    }

    private void sendNotification(Context context) {
        actionIntent = new Intent(context, OpenedNotificationActivity.class);
        mContext = context;

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

        // create notification
        Notification notification = isImageCategory(randomCategory) ? getImageNotification(randomCategory, context) : getTextNotification(randomCategory, context);
        notifManager.createNotificationChannel(channel);
        notifManager.notify(NOTIFICATION_ID, notification);
    }

    public Notification getTextNotification(String category, Context context) {
        notificationLayout.setTextViewText(R.id.notification_text, category + "!");
        notificationLayoutExpanded.setTextViewText(R.id.notification_text_expanded, category + "!");

        String content = (String) getContent(category);
        notificationLayoutExpanded.setTextViewText(R.id.notification_text_main_expanded, content);

        // Prepare action intent
        actionIntent.putExtra("category", category);
        actionIntent.putExtra("text", content);

        actionPendingIntent = PendingIntent.getActivity(context, 0,
                actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder textNotification = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.small_icon)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayoutExpanded)
                .setContentIntent(actionPendingIntent);

        return textNotification.build();
    }

    public Notification getImageNotification(String category, Context context) {
        notificationLayout.setTextViewText(R.id.notification_text, "Open Me!");
        int content = (int) getContent(category);

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), content);
        notificationLayout.setImageViewBitmap(R.id.notification_image, bitmap);

        notificationLayoutExpanded.setImageViewBitmap(R.id.notification_image_expanded, bitmap);

        // Prepare action intent
        actionIntent.putExtra("category", category);
        actionIntent.putExtra("img", content);

        actionPendingIntent = PendingIntent.getActivity(context, 0,
                actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

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
        return categories.get(randomInt);
    }

    private Object getContent(String category) {
        ArrayList<Object> contentArray = Content.contentMap.get(category);
        Random random = new Random();
        assert contentArray != null;
        int randomInt = random.nextInt(contentArray.size());
        return contentArray.get(randomInt);
    }

    private boolean isImageCategory(String category) {
        return category.equals("Animals") || category.equals("Nature") || category.equals("Travel");
    }
}
