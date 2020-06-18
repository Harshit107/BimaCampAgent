package com.teknesya.jeevanbimacamp.Utils;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.teknesya.jeevanbimacamp.R;
import com.teknesya.jeevanbimacamp.notification.NotificationReminder;
import com.teknesya.jeevanbimacamp.notification.Notification_channel;

import java.util.Calendar;

public class BroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Tag","here");
        Calendar c=Calendar.getInstance();

        Vibrator vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);
        NotificationReminder n=new NotificationReminder(context,"Alarn","We have Successfully Set");



    }
}
