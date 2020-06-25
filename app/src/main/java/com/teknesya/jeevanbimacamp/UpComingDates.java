package com.teknesya.jeevanbimacamp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public class UpComingDates extends AppCompatActivity {

    FrameLayout fBirthDay,fAnniver,fPremium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_comming_dates);

        fBirthDay=findViewById(R.id.fUpcommingBirdthday);
        fAnniver=findViewById(R.id.fUpAnni);
        fPremium=findViewById(R.id.fPremium);

        fBirthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        fPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        fAnniver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }
}
