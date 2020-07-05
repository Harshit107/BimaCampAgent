package com.teknesya.jeevanbimacamp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.teknesya.jeevanbimacamp.Adapter.LeadViewAdapter;
import com.teknesya.jeevanbimacamp.AgentTodaysReminder;
import com.teknesya.jeevanbimacamp.AlarmSetting;
import com.teknesya.jeevanbimacamp.CreateNewLead;
import com.teknesya.jeevanbimacamp.R;
import com.teknesya.jeevanbimacamp.SharedPreferenceValue;
import com.teknesya.jeevanbimacamp.TabFragment.FreshCall;
import com.teknesya.jeevanbimacamp.UpComingDates;
import com.teknesya.jeevanbimacamp.ViewLead;

import de.hdodenhof.circleimageview.CircleImageView;


public class ServiceFragment extends Fragment {

    private  CircleImageView reminderIcon;
    private  TextView changeTextReminder;
    private  FrameLayout fSetReminder;
   private FrameLayout fCreateLead;
    private FrameLayout fViewLead;
    private FrameLayout fTodaysReminder;
    private FrameLayout fFreshCall;
    private FrameLayout fUpcommingEvent;
    ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fSetReminder = getActivity().findViewById(R.id.fSetReminder);
        fTodaysReminder = getActivity().findViewById(R.id.fTodayReminder);
        fFreshCall = getActivity().findViewById(R.id.fFreshReminder);
        reminderIcon = getActivity().findViewById(R.id.reminderIcon);
        changeTextReminder = getActivity().findViewById(R.id.remindertext);
        fUpcommingEvent = getActivity().findViewById(R.id.fUpcommingBirdthday);

        updateReminderIcon();

        fSetReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getContext(), AlarmSetting.class);
                startActivity(it);
            }
        });
        fUpcommingEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getContext(), UpComingDates.class);
                startActivity(it);
            }
        });

        fFreshCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Fragment f = new FreshCall();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.framelayout, f);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        fTodaysReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getContext(), AgentTodaysReminder.class);
                startActivity(it);
            }
        });

        fCreateLead = getActivity().findViewById(R.id.fAddLead);
        fCreateLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getContext(), CreateNewLead.class);
                startActivity(it);
            }
        });
        TextView textview = (TextView) getActivity().findViewById(R.id.tv_name);
        textview.setText("Service");

        fViewLead = getActivity().findViewById(R.id.fViewLead);
        fViewLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getContext(), ViewLead.class);
                startActivity(it);
            }
        });


    }

    private void updateReminderIcon() {

        SharedPreferenceValue sv = new SharedPreferenceValue(getContext());
        String rem = sv.getReminder();

        if (!rem.equals("-1")) {
            reminderIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_alarm_on_black_24dp));
            changeTextReminder.setText("Change Reminder");
        }
    }
}
