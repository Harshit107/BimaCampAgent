package com.teknesya.jeevanbimacamp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.teknesya.jeevanbimacamp.TabFragment.PendingReferral;
import com.teknesya.jeevanbimacamp.TabFragment.SuccessReferral;

public class AgentReferralHistory extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;

    PendingReferral pendingReferral;
    SuccessReferral successReferral;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_referral_history);

        viewPager=findViewById(R.id.viewPager);
        tabLayout=findViewById(R.id.tablayout);

        pendingReferral=new PendingReferral();
        successReferral=new SuccessReferral();

        MyPagerAddapter myPagerAddapter=new MyPagerAddapter(getSupportFragmentManager());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(myPagerAddapter);



    }
    class MyPagerAddapter extends FragmentPagerAdapter{

        String fragmentName[]={
                "Success Referral",
                "Pending Referral"
        };

        public MyPagerAddapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:
                    return successReferral;
                case 1:
                    return pendingReferral ;
            }
            return successReferral;
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
