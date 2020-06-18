package com.teknesya.jeevanbimacamp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teknesya.jeevanbimacamp.notification.NotificationReminder;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;
import es.dmoral.toasty.Toasty;

public class PresentationView extends AppCompatActivity {

    TextView walletBalance, presentation, insufficient;
    Button butNow;
    int rechargingAmount =-1;
    int presentationValue=0;

    View progress;
    TextView pText;
    FirebaseAuth mAuth;
    String walletPrevAmount, presentationPrevValue;
    DatabaseReference mRoot, updateAmount, updatePresentation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation_view);

        insufficient=findViewById(R.id.insufficient);
        walletBalance = findViewById(R.id.wallet_balance);
        presentation = findViewById(R.id.presentation);
        butNow = findViewById(R.id.but_bt);
        mRoot = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        progress = findViewById(R.id.progress_bar);
        pText = progress.findViewById(R.id.progress_text);
        progress.setVisibility(View.GONE);

        getAmountFromDatabase();
        getPrevPresentationFromDatabase();

        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), AgentMainActivity.class));
                //overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        butNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rechargingAmount ==-1)
                    Toasty.error(getApplicationContext(),"Please Select Presentation Value",Toasty.LENGTH_LONG,true).show();
                else {
                    if (rechargingAmount == 1)
                        presentationValue = 1;
                    else if (rechargingAmount == 4)
                        presentationValue = 5;
                    else if (rechargingAmount == 6)
                        presentationValue = 10;
                    succesPayment();
                }




            }
        });


        final RadioRealButton button1 = (RadioRealButton) findViewById(R.id.button1);
        final RadioRealButton button2 = (RadioRealButton) findViewById(R.id.button2);

        RadioRealButtonGroup group = (RadioRealButtonGroup) findViewById(R.id.group);

// onClickButton listener detects any click performed on buttons by touch
        group.setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(RadioRealButton button, int position) {
                rechargingAmount = getAmount(position);
                Toasty.info(PresentationView.this, String.valueOf(rechargingAmount), Toasty.LENGTH_SHORT, true).show();


            }
        });

// onPositionChanged listener detects if there is any change in position
        group.setOnPositionChangedListener(new RadioRealButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(RadioRealButton button, int currentPosition, int lastPosition) {
                 //Toasty.info(PresentationView.this,  currentPosition,Toasty.LENGTH_SHORT,true).show();
                rechargingAmount = getAmount(currentPosition);
            }


        });

// onLongClickedButton detects long clicks which are made on any button in group.
// return true if you only want to detect long click, nothing else
// return false if you want to detect long click and change position when you release
        group.setOnLongClickedButtonListener(new RadioRealButtonGroup.OnLongClickedButtonListener() {
            @Override
            public boolean onLongClickedButton(RadioRealButton button, int position) {
               // Toast.makeText(PresentationView.this, "Long Clicked! Position: " + position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private int getAmount(int position) {
        if (position == 0)
            return 1;
        if (position == 1)
            return 4;
        if (position == 2)
            return 6;

        return -1;
    }

    public void succesPayment() {
        progress.setVisibility(View.VISIBLE);
        getAmountFromDatabase();

        if (validateAmount(rechargingAmount)) {
            updateAmountToDatabase(String.valueOf(rechargingAmount));
        } else
            progress.setVisibility(View.GONE);
    }

    private void updateAmountToDatabase(String amoubtToAdd) {
        pText.setText("Updating Presentation...");
        int amountToBeAdded =  (Integer.parseInt(walletPrevAmount)-(Integer.parseInt(amoubtToAdd))) ;
        updateAmount = mRoot.child("users").child("customer")
                .child("registered").child("detail").child(mAuth.getUid());
        updateAmount.child("wallet").setValue(amountToBeAdded)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       setUpdatePresentationToDatabase(String.valueOf(presentationValue));

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.setVisibility(View.GONE);
                Toasty.error(getApplicationContext(), e.getMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });

    }

    private void setUpdatePresentationToDatabase(String presentation) {
        int presentToBeAdded = (Integer.parseInt(presentation) + Integer.parseInt(presentationPrevValue));
        updateAmount = mRoot.child("users").child("customer")
                .child("registered").child("detail").child(mAuth.getUid());
        updateAmount.child("presentation").setValue(presentToBeAdded)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pText.setText("Success");
                        NotificationReminder notificationReminder=new NotificationReminder(getApplicationContext()
                                ,"Presentation Added Successful",
                                "Thankyou for Using JeevanBimacamp.Use Your Presentation Before it gets Expired");

                        Toasty.success(getApplicationContext(), "Recharge Successful", Toasty.LENGTH_LONG, true).show();
                        Intent it = new Intent(getApplicationContext(), PresentationView.class);
                        progress.setVisibility(View.GONE);
                        finish();
                        startActivity(it);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.setVisibility(View.GONE);
                Toasty.error(getApplicationContext(), e.getMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });

    }



    private boolean validateAmount(int rechargeAmount) {

        if(Integer.parseInt(walletPrevAmount)<rechargeAmount) {
            new AlertDialog.Builder(PresentationView.this)
                    .setTitle("Insufficient Balance")
                    .setMessage("Recharge Your Wallet to Buy More Presentation")
                    .setPositiveButton("Recharge", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent it=new Intent(getApplicationContext(), RechargeWallet.class);
                            startActivity(it);

                        }
                    })
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            insufficient.setVisibility(View.VISIBLE);
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
            return false;
        }


        return true;
    }


    public void getAmountFromDatabase() {
        pText.setText("Validating Wallet Balance..");
        DatabaseReference getPrevAmount = mRoot.child("users").child("customer")
                .child("registered").child("detail").child(mAuth.getUid());
        getPrevAmount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("wallet"))
                    walletPrevAmount = dataSnapshot.child("wallet").getValue().toString();
                else
                    walletPrevAmount = "0";
                walletBalance.setText(walletPrevAmount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.info(getApplicationContext(), databaseError.getMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });
    }


    public void getPrevPresentationFromDatabase() {
        DatabaseReference getPrevAmount = mRoot.child("users").child("customer")
                .child("registered").child("detail").child(mAuth.getUid());
        getPrevAmount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("presentation"))
                    presentationPrevValue = dataSnapshot.child("presentation").getValue().toString();
                else
                    presentationPrevValue = "0";
                presentation.setText(presentationPrevValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.info(getApplicationContext(), databaseError.getMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });
    }

}