package com.teknesya.jeevanbimacamp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class ShareAndEarn extends AppCompatActivity {

    ImageView referView,share_bt;
    TextView persentationReferral;
    TextView referralCode;

    FirebaseAuth mAuth;
    DatabaseReference mRoot;
    DatabaseReference dRef;
    String referralCodeValue="",referralCost="";
    View progress;
    TextView ptext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_and_earn);
        mAuth=FirebaseAuth.getInstance();
        mRoot= FirebaseDatabase.getInstance().getReference();
        progress = findViewById(R.id.progress_bar);
        ptext = progress.findViewById(R.id.progress_text);
        progress.setVisibility(View.VISIBLE);

        referView=findViewById(R.id.refer_view);
        share_bt=findViewById(R.id.share_bt);
        persentationReferral=findViewById(R.id.referral_cost);
        referralCode=findViewById(R.id.share_code_tv);

        referView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(getApplicationContext(),AgentReferralHistory.class);
                startActivity(it);
            }
        });

        dRef=mRoot.child("users").child("customer").child("registered").child("detail")
                .child(mAuth.getUid());
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("referralcode")) {
                    referralCodeValue = dataSnapshot.child("referralcode").getValue().toString();
                    referralCode.setText("Your Code is : "+referralCodeValue);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRoot.child("unique").child("referral").child("value")
               .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("referral")) {
                    referralCost = dataSnapshot.child("referral").getValue().toString();
                    persentationReferral.setText(referralCost);
                }
                progress.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(getApplicationContext(),databaseError.getMessage(),Toasty.LENGTH_LONG,true).show();
            }
        });










        share_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Select App"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });

    }
}
