package com.teknesya.jeevanbimacamp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class UpComingDates extends AppCompatActivity {

    FrameLayout fBirthDay,fAnniver,fPremium,fLead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_coming_dates);

        fBirthDay=findViewById(R.id.fUpcommingBirdthday);
        fAnniver=findViewById(R.id.fUpAnni);
        fPremium=findViewById(R.id.fPremium);
        fLead=findViewById(R.id.fUpLead);

        ImageView back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fBirthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(getApplicationContext(),UpComingEventDetail.class);
                it.putExtra("type","birthday");
                startActivity(it);
            }
        });

        fPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(getApplicationContext(),UpComingEventDetail.class);
                it.putExtra("type","premium");
                startActivity(it);
            }
        });

        fAnniver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(getApplicationContext(),UpComingEventDetail.class);
                it.putExtra("type","marriage");
                startActivity(it);
            }
        });
        fLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(getApplicationContext(),UpComingEventDetail.class);
                it.putExtra("type","lead");
                startActivity(it);
            }
        });



    }
}
