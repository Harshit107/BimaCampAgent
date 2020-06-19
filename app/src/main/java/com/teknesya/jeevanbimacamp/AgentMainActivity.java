package com.teknesya.jeevanbimacamp;

import android.content.DialogInterface;
import android.content.Intent;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.teknesya.jeevanbimacamp.Fragment.HomeFragment;
import com.teknesya.jeevanbimacamp.Fragment.ServiceFragment;
import com.teknesya.jeevanbimacamp.Fragment.SettingFragment;
import com.teknesya.jeevanbimacamp.model.IOnBackPressed;

import es.dmoral.toasty.Toasty;

public class AgentMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth mAuth;
    ImageView update, profile;

    DrawerLayout drawer;
    NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private RelativeLayout content;
    protected IOnBackPressed onBackPressedListener;

    TextView tv_name;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_main);
        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        content = findViewById(R.id.parent);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        tv_name = findViewById(R.id.tv_name);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(AgentMainActivity.this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
                    private float scaleFactor = 6f;

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        super.onDrawerSlide(drawerView, slideOffset);
                        float slideX = drawerView.getWidth() * slideOffset;
                        //  Log.d("slide", "onDrawerSlide: " + slideOffset + "slidex" + slideX);

                        content.setTranslationX(slideX);
                        //  drawer.setTranslationY(slideX/2);
                        content.setScaleX(1 - (slideOffset / scaleFactor));
                        content.setScaleY(1 - (slideOffset / scaleFactor));
                        // drawer.setScaleX(1 - (slideOffset / scaleFactor));
                        //drawer.setScaleY(1 - (slideOffset / scaleFactor));

                    }
                };
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new HomeFragment()).commit();

        drawer.setScrimColor(Color.TRANSPARENT);
        drawer.setDrawerElevation(0f);
        drawer.addDrawerListener(actionBarDrawerToggle);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        // Remove back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        // Show hamburger


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            Intent it = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(it);
        }
    }
//    @Override public void onBackPressed() {
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.framelayout);
//        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
//
//        }
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawers();
        switch (item.getItemId()) {
            case R.id.nav_profile:
                Intent animation=new Intent(getApplicationContext(),AgentProfile.class);
                startActivity(animation);
                return true;

            case R.id.nav_presentation:
                Intent it=new Intent(getApplicationContext(), BuyPresentation.class);
                startActivity(it);
                return true;

              case R.id.nav_rechargeWallet:
                Intent recharge=new Intent(getApplicationContext(),RechargeWallet.class);
                startActivity(recharge);
                return true;
            case R.id.nav_share:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Select App"));
                } catch(Exception e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.nav_logout:
                mAuth.signOut();
                finish();
                Intent logOut=new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(logOut);
                Toasty.success(getApplicationContext(),"Logout Successful").show();
                return true;

        }
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectFragment = new HomeFragment();
            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectFragment = new HomeFragment();
                    break;
                case R.id.nav_wallet:
                    selectFragment = new ServiceFragment();
                    break;

                case R.id.account:
                    selectFragment = new SettingFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, selectFragment).commit();

            return true;
        }
    };

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AgentMainActivity.this);

            builder.setMessage("Do You Want To exit ?");
            builder.setTitle(R.string.app_name)
                    .setIcon(getResources().getDrawable(R.drawable.logo));
            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent it = new Intent(getApplicationContext(), Exit.class);
                    it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(it);

                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }



}