package com.teknesya.jeevanbimacamp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.teknesya.jeevanbimacamp.Utils.BroadCastReceiver;
import com.teknesya.jeevanbimacamp.Utils.TimePickerFragment;

import java.text.DateFormat;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class AlarmSetting extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    Button create;
    TextView tv;
    static  int p=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        create=findViewById(R.id.create);
        tv=findViewById(R.id.tv);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker=new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"TimePicker");

            }
        });
    }

//    private void cancelAlarm() {
//
//        AlarmManager alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent=new Intent(this, BroadCastReceiver.class);
//        PendingIntent pendingIntent=PendingIntent
//                .getBroadcast(this,1,intent,0);
//        alarmManager.cancel(pendingIntent);
//    }


    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Calendar c=Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,i);
        c.set(Calendar.MINUTE,i1);
        c.set(Calendar.SECOND,0);

        updateAlarm(c);
        StartAlarm(c);

    }

    private void StartAlarm(Calendar c) {
        String timetext="Reminder Set For : ";
        timetext += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        tv.setText(timetext);
        tv.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

    }

    private void updateAlarm(Calendar c) {

        p++;
        AlarmManager alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(this, BroadCastReceiver.class);
        PendingIntent pendingIntent=PendingIntent
                .getBroadcast(this,p,intent,0);
        assert alarmManager != null;
        if(c.before(Calendar.getInstance()))
            c.add(Calendar.DATE,1);
        updateSharedPreference(DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime()));
       // alarmManager.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
        Toasty.success(getApplicationContext(),"Reminder set successfully").show();
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);



    }

    public void updateSharedPreference(String c)
    {
        SharedPreferenceValue sv=new SharedPreferenceValue(getApplicationContext());
        sv.updateReminder(c);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferenceValue sv=new SharedPreferenceValue(getApplicationContext());
        String rem=sv.getReminder();

        if(!rem.equals("-1")) {
            tv.setText("Daily Reminder Set For : " +rem);
            tv.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }

    }
}
