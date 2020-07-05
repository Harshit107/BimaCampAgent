package com.teknesya.jeevanbimacamp.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.teknesya.jeevanbimacamp.R;
import com.teknesya.jeevanbimacamp.SplashScrActivity;

import java.util.Calendar;

public class NotificationReminder {

    Context context;
    NotificationManagerCompat notificationManager;
    Activity activity;

    public  NotificationReminder(Context context,String title,String message)
    {

        Intent it=new Intent(context,SplashScrActivity.class);  //click on notification action
        final PendingIntent pendingIntent=PendingIntent.getActivity(context,0,it,0);

        this.context=context;
        Bitmap largeIcon= BitmapFactory
                .decodeResource(context.getResources(),R.drawable.logo);//image to display
        notificationManager=NotificationManagerCompat.from(context);
        Notification notification=new NotificationCompat.Builder(context,
                Notification_channel.CHANNEL_ID_1)
                .setSmallIcon(R.drawable.icon_image)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setLargeIcon(largeIcon)  //side image
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .build();
                 notificationManager.notify((int)(Calendar.getInstance().getTimeInMillis()/1000),notification);

    }
    public  NotificationReminder(Context context,String title,String message,Activity activity)
    {

        Intent it=new Intent(context,activity.getClass());  //click on notification action
        final PendingIntent pendingIntent=PendingIntent.getActivity(context,0,it,0);

        this.context=context;
        Bitmap largeIcon= BitmapFactory
                .decodeResource(context.getResources(),R.drawable.logo);//image to display
        notificationManager=NotificationManagerCompat.from(context);
        Notification notification=new NotificationCompat.Builder(context,
                Notification_channel.CHANNEL_ID_1)
                .setSmallIcon(R.drawable.icon_image)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setLargeIcon(largeIcon)  //side image
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .build();
        notificationManager.notify((int)(Calendar.getInstance().getTimeInMillis()/1000),notification);

    }





}
