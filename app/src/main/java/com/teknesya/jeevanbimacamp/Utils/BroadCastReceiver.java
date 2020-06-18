package com.teknesya.jeevanbimacamp.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teknesya.jeevanbimacamp.notification.NotificationReminder;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;

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


            checkUserDates.addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String recivedDate;
                        recivedDate = snapshot.getKey().toString();

                        if (recivedDate.startsWith(DateBima.getDateMonth())) {
                            Vibrator vibrator = (Vibrator) context
                                    .getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(2000);
                            NotificationReminder n = new NotificationReminder
                                    (context, "Alarm", "Birthday reminder for Tomorrow ");

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toasty.error(context, databaseError.getMessage()).show();
                }
            });
        }
        else {
            NotificationReminder notificationReminder=new NotificationReminder
                    (context,"Reminder","Connection Not Available");
        }







    }
}
