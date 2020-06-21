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

import com.teknesya.jeevanbimacamp.Adapter.LeadViewAdapter;
import com.teknesya.jeevanbimacamp.AgentTodaysReminder;
import com.teknesya.jeevanbimacamp.AlarmSetting;
import com.teknesya.jeevanbimacamp.CreateNewLead;
import com.teknesya.jeevanbimacamp.R;
import com.teknesya.jeevanbimacamp.ViewLead;


public class ServiceFragment extends Fragment {

    FrameLayout fSetReminder;
    FrameLayout fCreateLead,fViewLead,fTodaysReminder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fSetReminder=getActivity().findViewById(R.id.fSetReminder);
        fTodaysReminder=getActivity().findViewById(R.id.fTodayReminder);

        fSetReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(getContext(),AlarmSetting.class);
                startActivity(it);
            }
        });

        fTodaysReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(getContext(), AgentTodaysReminder.class);
                startActivity(it);
            }
        });

        fCreateLead=getActivity().findViewById(R.id.fAddLead);
        fCreateLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(getContext(), CreateNewLead.class);
                startActivity(it);
            }
        });
        TextView textview = (TextView)getActivity().findViewById(R.id.tv_name);
        textview.setText("Service");

        fViewLead=getActivity().findViewById(R.id.fViewLead);
        fViewLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(getContext(), ViewLead.class);
                startActivity(it);
            }
        });



    }
}
