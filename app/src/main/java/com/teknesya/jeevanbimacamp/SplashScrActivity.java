package com.teknesya.jeevanbimacamp;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teknesya.jeevanbimacamp.Utils.InternetAvailablity;

import es.dmoral.toasty.Toasty;

public class SplashScrActivity extends AppCompatActivity {

    private static final String TAG ="TAG" ;
    private ImageView logoSplash, logoWhite;
    LinearLayout chmaraTech;
    private Animation anim1, anim2, anim3;

    FirebaseUser firebaseUser;
    FirebaseAuth maAth;
    DatabaseReference root,checkuser;
    String user="customer";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_scr);
        init();




        logoSplash.startAnimation(anim1);
        anim1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                logoSplash.startAnimation(anim2);
                logoSplash.setVisibility(View.GONE);

                logoWhite.startAnimation(anim3);
                chmaraTech.startAnimation(anim3);
                anim3.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        logoWhite.setVisibility(View.VISIBLE);
                        chmaraTech.setVisibility(View.VISIBLE);

                        if (isInternet()) {

                            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                            if (firebaseUser == null) {
                                finish();
                                startActivity(new Intent(SplashScrActivity.this, RegistrationActivity.class));
                                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                            } else {
                                checkUserFromDatabase();
                            }


                        }
                        else
                        {
                            Toasty.error(getApplicationContext(),"Internet is not Available",Toasty.LENGTH_LONG,true).show();
                            startActivity(new Intent(SplashScrActivity.this, Nointernate.class));
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });




    }

    private void checkUserFromDatabase() {

        checkuser=root.child("checkuser");
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if(dataSnapshot.hasChild(maAth.getUid())) {
                        user = dataSnapshot.child(maAth.getUid()).getValue().toString();
                        senduser();
                    }
                    else{
                        user="customer";
                        senduser();
                    }

                }catch (Exception e)
                {
                    senduser();
                    Toasty.error(getApplicationContext(),"Error "+e.getMessage(),3000,true).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                senduser();
                Toasty.error(getApplicationContext(),"Error "+databaseError.getMessage(),5000,true).show();
            }
        });



    }
    void senduser()
    {

        finish();
        if (user.equals("customer")){
            startActivity(new Intent(SplashScrActivity.this, CustHomePage.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        }
        else if (user.equals("agent")) {
            startActivity(new Intent(SplashScrActivity.this, AgentMainActivity.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        }
        else {
            startActivity(new Intent(SplashScrActivity.this, LoginActivity.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        }
    }


    private void init(){

        maAth=FirebaseAuth.getInstance();
        root= FirebaseDatabase.getInstance().getReference();
        logoSplash = findViewById(R.id.ivLogoSplash);
        logoWhite = findViewById(R.id.ivLogoWhite);
        chmaraTech = findViewById(R.id.ivCHTtext);




        anim1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        anim2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadeout);
        anim3 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadein);

    }
    public boolean isInternet()
    {
        InternetAvailablity internetAvailablity=new InternetAvailablity(getApplicationContext());
        return internetAvailablity.isInternetOn();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isInternet()) {

            Toasty.error(getApplicationContext(),"Internet is not Available",Toasty.LENGTH_LONG,true).show();
            startActivity(new Intent(SplashScrActivity.this, Nointernate.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }
    }
}
