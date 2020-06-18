package com.teknesya.jeevanbimacamp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class AgentBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_base);
    }
    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
           Intent it=new Intent(getApplicationContext(),AgentMainActivity.class);
           startActivity(it);
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }
}
