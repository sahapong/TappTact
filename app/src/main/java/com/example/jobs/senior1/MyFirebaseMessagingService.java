package com.example.jobs.senior1;

/**
 * Created by jobs on 26-Jan-17.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private final String TAG= "MyFirebase";
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String, String> data = remoteMessage.getData();

        sendNotification(notification, data);
    }

    /**
     * Create and show a custom notification containing the received FCM message.
     *
     * @param notification FCM notification payload received.
     * @param data FCM data payload received.
     */
    private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) {
//        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Bitmap bigIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        try {
            String picture_url = data.get("img");
            if (picture_url != null && !"".equals(picture_url)) {
                URL url = new URL(picture_url);
                bigIcon = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                notificationBuilder.setStyle(
//                        new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(notification.getBody())
//                );
//                notificationBuilder.setLargeIcon(bigPicture);
                Log.d(TAG,"setImage");
            }
        } catch (IOException e) {
            Log.d(TAG,"error: "+e.getMessage());
            Log.d(TAG,"error image: "+data.get("img"));
            e.printStackTrace();
        }
        PendingIntent pendingIntent;
        if(notification.getBody().compareTo("You received a new card!")==0) {
            Intent intent = new Intent(this, InboxActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }
        else{

            Intent intent = new Intent(this, CardDetailActivity.class);
            intent.putExtra("cardId",data.get("card"));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }

        final SharedPreferences examplePrefs = getSharedPreferences("Notification",0);

        if(examplePrefs.getBoolean("Notification", true)) {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(notification.getTitle())
                    .setContentText(notification.getBody())
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent)
//                .setContentInfo(notification.getTitle())
                    .setLargeIcon(bigIcon)
                    .setPriority(Notification.PRIORITY_HIGH)
//                .setColor(Color.RED)
                    .setSmallIcon(R.mipmap.ic_launcher);

            try {
                String picture_url = data.get("image");
                if (picture_url != null && !"".equals(picture_url)) {
                    URL url = new URL(picture_url);
                    Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                notificationBuilder.setStyle(
//                        new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(notification.getBody())
//                );
                    notificationBuilder.setLargeIcon(bigPicture);
                    Log.d(TAG, "setImage");
                }
            } catch (IOException e) {
                Log.d(TAG, e.getMessage());
                e.printStackTrace();
            }
        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setLights(Color.YELLOW, 1000, 300);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
        }
    }
}