package com.teknesya.jeevanbimacamp.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Vibrator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teknesya.jeevanbimacamp.notification.NotificationReminder;

import java.util.ArrayList;

public class BroadCastReceiver extends BroadcastReceiver {

    FirebaseAuth mAuth;
    DatabaseReference mRoot,checkUserDates,nameDatabase;
    ArrayList<String > name=new ArrayList<>();
    int count=0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(final Context context, Intent intent) {

        InternetAvailablity i=new InternetAvailablity(context);
        if(i.isInternetOn()) {

            mAuth = FirebaseAuth.getInstance();
            mRoot = FirebaseDatabase.getInstance().getReference();
            checkUserDates = mRoot.child("users").child("agent").child(mAuth.getUid()).child("date");
            nameDatabase = mRoot.child("users").child("agent").child(mAuth.getUid()).child("customer");


            checkUserDates.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String recivedDate;
                    recivedDate = dataSnapshot.getKey().toString();

                    if (recivedDate.startsWith(DateBima.getTommorowDateMonth())) {
                        Vibrator vibrator = (Vibrator) context
                                .getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(2000);
                        NotificationReminder n = new NotificationReminder
                                (context, "Alarm", "Birthday reminder for Tomorrow ");

                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String recivedDate;
                    recivedDate = dataSnapshot.getKey().toString();

                    if (recivedDate.startsWith(DateBima.getTommorowDateMonth())) {
                        Vibrator vibrator = (Vibrator) context
                                .getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(2000);
                        NotificationReminder n = new NotificationReminder
                                (context, "Alarm", "Birthday reminder for Tomorrow ");

                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            NotificationReminder notificationReminder=new NotificationReminder
                    (context,"Reminder","Connection Not Available");
        }







    }
}
