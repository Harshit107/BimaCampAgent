package com.teknesya.jeevanbimacamp.notification;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class Notification_channel extends Application {
    public static final String CHANNEL_ID_1="Channel 1";
    public static final String CHANNEL_ID_2="Channel 2";


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        createnotification();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createnotification() {

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {

            NotificationChannel channel1 = new NotificationChannel(CHANNEL_ID_1,
                    "Recharge", NotificationManager.IMPORTANCE_DEFAULT);
            channel1.setDescription("Notification From Recharge Section");

            NotificationChannel channel2 = new NotificationChannel(CHANNEL_ID_2,
                    "Custom,", NotificationManager.IMPORTANCE_DEFAULT);
            channel1.setDescription("Custom Notification");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }
}
