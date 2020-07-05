package com.teknesya.jeevanbimacamp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.teknesya.jeevanbimacamp.TabFragment.Anniversary;
import com.teknesya.jeevanbimacamp.TabFragment.FreshCall;
import com.teknesya.jeevanbimacamp.TabFragment.PendingReferral;
import com.teknesya.jeevanbimacamp.TabFragment.SuccessReferral;
import com.teknesya.jeevanbimacamp.TabFragment.BirthDay;

public class AgentTodaysReminder extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;

    BirthDay birthDay;
    Anniversary anniversary;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_todays_reminder);

        viewPager= (ViewPager) findViewById(R.id.viewPager);
        tabLayout=findViewById(R.id.tablayout);

        MyPagerAddapter myPagerAddapter=new MyPagerAddapter(getSupportFragmentManager());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(myPagerAddapter);

        ImageView back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });








    }
    class MyPagerAddapter extends FragmentPagerAdapter{

        String fragmentName[]={
                "Birthday",
                "Anniversary"
        };

        public MyPagerAddapter(@NonNull FragmentManager fm) {
            super(fm);
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment selected=null;

            switch (position)
            {
                case 0:
                    birthDay=new BirthDay();
                    selected=birthDay;
                    break;
                case 1:
                    anniversary=new Anniversary();
                    selected=anniversary;
                    break;

                default:selected=null;

            }
            return selected;
        }

        @Override
        public int getCount() {
            return fragmentName.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentName[position];
        }

    }



}
