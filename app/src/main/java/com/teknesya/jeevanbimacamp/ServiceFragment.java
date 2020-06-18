package com.teknesya.jeevanbimacamp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;


public class ServiceFragment extends Fragment {

    FrameLayout fSetReminder;
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
        fSetReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(getContext(),AlarmSetting.class);
                startActivity(it);
            }
        });

        TextView textview = (TextView)getActivity().findViewById(R.id.tv_name);
        textview.setText("Service");


    }
}
