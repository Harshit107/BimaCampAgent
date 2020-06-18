package com.teknesya.jeevanbimacamp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Nointernate extends AppCompatActivity {
    TextView internet;
    Animation fadeinanimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nointernate);
        Button back=(Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });
        internet=(TextView)findViewById(R.id.internet);
        fadeinanimation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.side_in_left);
        internet.setVisibility(View.VISIBLE);
        internet.setAnimation(fadeinanimation);

    }
    @Override
    public void onBackPressed() {

        finish();


    }
}
