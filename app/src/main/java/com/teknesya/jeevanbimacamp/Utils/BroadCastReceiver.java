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
    String msg="";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(final Context context, Intent intent) {


        try
        {
             msg=intent.getStringExtra("msg");
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        //checking Internet Connectivity

        InternetAvailablity i=new InternetAvailablity(context);
        //if Internet is Connected
        if(i.isInternetOn()) {
        // Checking message Type
            mAuth = FirebaseAuth.getInstance();
            mRoot = FirebaseDatabase.getInstance().getReference();
            checkUserDates = mRoot.child("users").child("agent").child(mAuth.getUid()).child("date");
            nameDatabase = mRoot.child("users").child("agent").child(mAuth.getUid()).child("customer");

            if(msg.equals("today")){

                checkUserDates.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String recivedDate;
                        recivedDate = dataSnapshot.getKey().toString();

                        if (recivedDate.startsWith(DateBima.getTodaysDateMonth())) {

                            if(dataSnapshot.hasChild("lead"))
                            {
                                Vibrator vibrator = (Vibrator) context
                                        .getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(2000);
                                NotificationReminder n = new NotificationReminder
                                        (context, "Reminder", "You have Fresh Call Today\nClick here To Check");
                            }
                            if(dataSnapshot.hasChild("marriage")){
                                Vibrator vibrator = (Vibrator) context
                                        .getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(2000);
                                NotificationReminder n = new NotificationReminder
                                        (context, "Reminder", "You have Anniversary Reminder Today\nClick here To Check");
                            }
                            if(dataSnapshot.hasChild("birthday")){
                                Vibrator vibrator = (Vibrator) context
                                        .getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(2000);
                                NotificationReminder n = new NotificationReminder
                                        (context, "Reminder", "You have new birthday Reminder Today\nClick here To Check");
                            }

                        }
                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String recivedDate;
                        recivedDate = dataSnapshot.getKey().toString();

                        if (recivedDate.startsWith(DateBima.getTodaysDateMonth())) {

                            if(dataSnapshot.hasChild("lead"))
                            {
                                Vibrator vibrator = (Vibrator) context
                                        .getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(2000);
                                NotificationReminder n = new NotificationReminder
                                        (context, "Reminder", "You have Fresh Call Today\nClick here To Check");
                            }
                            if(dataSnapshot.hasChild("marriage")){
                                Vibrator vibrator = (Vibrator) context
                                        .getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(2000);
                                NotificationReminder n = new NotificationReminder
                                        (context, "Reminder", "You have Anniversary Reminder Today\nClick here To Check");
                            }
                            if(dataSnapshot.hasChild("birthday")){
                                Vibrator vibrator = (Vibrator) context
                                        .getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(2000);
                                NotificationReminder n = new NotificationReminder
                                        (context, "Reminder", "You have new birthday Reminder Today\nClick here To Check");
                            }

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
            else if (msg.equals("tomorrow")){
                checkUserDates.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String recivedDate;
                        recivedDate = dataSnapshot.getKey().toString();

                        if (recivedDate.startsWith(DateBima.getTommorowDateMonth())) {

                            if (dataSnapshot.hasChild("lead")) {
                                Vibrator vibrator = (Vibrator) context
                                        .getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(2000);
                                NotificationReminder n = new NotificationReminder
                                        (context, "Reminder", "You have Fresh Call Tomorrow\nClick here To Check");
                            }
                            if (dataSnapshot.hasChild("marriage")) {
                                Vibrator vibrator = (Vibrator) context
                                        .getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(2000);
                                NotificationReminder n = new NotificationReminder
                                        (context, "Reminder", "You have Anniversary Reminder Tomorrow\nClick here To Check");
                            }
                            if (dataSnapshot.hasChild("birthday")) {
                                Vibrator vibrator = (Vibrator) context
                                        .getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(2000);
                                NotificationReminder n = new NotificationReminder
                                        (context, "Reminder", "You have birthday Reminder Tomorrow\nClick here To Check");
                            }
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
                                    (context, "Reminder", "New Reminder For Tomorrow\nClick here To View");

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


        }
        else {
            NotificationReminder notificationReminder=new NotificationReminder
                    (context,"Reminder","Connection Not Available");
        }







    }
}
