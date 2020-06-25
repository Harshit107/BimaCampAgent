package com.teknesya.jeevanbimacamp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teknesya.jeevanbimacamp.Utils.DateBima;
import com.teknesya.jeevanbimacamp.Utils.MailSender;
import com.teknesya.jeevanbimacamp.notification.NotificationReminder;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class CustHomePage extends AppCompatActivity {
    RelativeLayout main_rl;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference mRoot, updateinfo, update_refereal, change_account, delete_from_newuser,
            update_checkuser;
    Button move, becomeagent;
    String referral_value = null, nyReferralsuccessValue = "-1";
    int check = 0;
    View progress;
    String eMail = "";
    TextView ptext;
    String msg = "ThankYou For becoming Agent";
    String title = "JeevanBimacamp";
    DatabaseReference successReferral, removePending;
    String uId="";

    Button goForPresentation_bt;
    EditText presentationCode;
    TextView createdBy;

    String presentationString;
    int presentationCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_home_page);

        initilize();
        goForPresentation_bt.setClickable(false);
        goForPresentation_bt.setEnabled(false);


        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), AgentMainActivity.class);
                startActivity(it);
            }
        });
        becomeagent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                ptext.setText("Updating Account Please wait");
                success();
            }
        });

        presentationCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = presentationCode.getText().toString();
                Log.i("TextChange", text);
                if (text.length() > 9) {
                    createdBy.setText("Checking code..");
                    checkPresenation(text);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });
        goForPresentation_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(presentationCount>0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CustHomePage.this);
                    builder.setCancelable(false)
                            .setMessage("You Can View Presentation Only Twice from One Code\nClick View Button to Continue. ")
                            .setTitle("Share")
                            .setIcon(R.drawable.logo)
                            .setPositiveButton("View", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    DatabaseReference checkVideoShare = mRoot.child("users").child("agent").child(uId).child("presentation");
                                    checkVideoShare.child(presentationCode.getText().toString())
                                            .child("count").setValue(--presentationCount);


                                    Intent send = new Intent(CustHomePage.this, CustAnimationView.class);
                                    send.putExtra("string",presentationString.split("--") );
                                    startActivity(send);
                                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);


                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.create().show();


                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CustHomePage.this);
                    builder.setCancelable(false)
                            .setMessage("Presentation Count Exceeded. ")
                            .setTitle("Presentation")
                            .setIcon(R.drawable.logo)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                   dialogInterface.cancel();
                                }
                            });
                    builder.create().show();

                }
            }
        });

    }

    private void initilize() {

        progress = findViewById(R.id.progress_bar);
        ptext = progress.findViewById(R.id.progress_text);
        main_rl = findViewById(R.id.main_rl);
        move = findViewById(R.id.move);
        becomeagent = findViewById(R.id.becomeagent);
        createdBy = findViewById(R.id.created_by);
        goForPresentation_bt = findViewById(R.id.apply);
        presentationCode = findViewById(R.id.presentation_code);
        SharedPreferenceValue sv=new SharedPreferenceValue(getApplicationContext());
        sv.updateReminder("-1");
        //Toasty.info(getApplicationContext(),sv.getReminder()).show();

        main_rl.setVisibility(View.VISIBLE);
        mRoot = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        successReferral = mRoot.child("users").child("customer").child("registered");

        referral_value = Objects.requireNonNull(mAuth.getUid()).substring(1, 7);


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void success() {
        getReferralValue();

        change_account = mRoot.child("users").child("agent").child(mAuth.getUid()).child("detail");

        successReferral.child("detail").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("email"))
                    eMail = dataSnapshot.child("email").getValue().toString();

                if (dataSnapshot.hasChild("agentId")) {
                    final String Agent = dataSnapshot.child("agentId").getValue().toString();

                    // Toasty.success(getApplicationContext(),Agent).show();
                    final DatabaseReference updatePresentation = successReferral.child("detail").child(Agent);
                    updatePresentation.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild("presentation")) {
                                String p = dataSnapshot.child("presentation").getValue().toString();
                                updatePresentation.child("presentation").setValue(Integer.valueOf(p) + Integer.valueOf(nyReferralsuccessValue));

                            } else {
                                updatePresentation.child("presentation").setValue(Integer.valueOf(nyReferralsuccessValue));
                            }

                            successReferral.child("referral").child("success").child(Agent).child(mAuth.getUid()).setValue(DateBima.getDate());
                            successReferral.child("referral").child("pending").child(Agent).child(mAuth.getUid()).removeValue();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        change_account.child("date").setValue(DateBima.getDate());
        change_account.child("referralcode").setValue(referral_value);

        updateinfo = mRoot.child("users").child("customer")
                .child("registered").child("detail").child(mAuth.getUid());
        updateinfo.child("type").setValue("agent");
        updateinfo.child("referralcode").setValue(referral_value);

        try {
            delete_from_newuser = mRoot.child("users").child("customer")
                    .child("newUser");
            delete_from_newuser.child("detail").child(mAuth.getUid()).removeValue();
        } catch (Exception e) {
            e.printStackTrace();
        }


        update_refereal = mRoot.child("unique").child("referral");
        update_refereal.child("code").child(referral_value).child("id").setValue(mAuth.getUid());

        update_checkuser = mRoot.child("checkuser").child(mAuth.getUid());
        update_checkuser.setValue("agent").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                endProgress();
                NotificationReminder notificationReminder = new NotificationReminder(getApplicationContext()
                        , "Agent Registration Successful",
                        "Thank you for Using JeevanBimacamp.");
                MailSender mailSender = new MailSender(CustHomePage.this, msg, title, eMail);
                mailSender.sendEmail();
                Intent it = new Intent(getApplicationContext(), AgentMainActivity.class);
                startActivity(it);
                Toasty.success(getApplicationContext(), "Success", Toasty.LENGTH_LONG, true).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(getApplicationContext(), e.getMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });

    }

    public void endProgress() {
        progress.setVisibility(View.GONE);
    }


    public void getReferralValue() {

        DatabaseReference updatePresentation = mRoot.child("unique")
                .child("referral").child("value");
        updatePresentation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("referral")) {
                    nyReferralsuccessValue = dataSnapshot.child("referral").getValue().toString();
                } else
                    nyReferralsuccessValue = "0";


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void checkPresenation(final String code) {

        DatabaseReference checkcode = mRoot.child("unique").child("presentation");
        final DatabaseReference checkPresentationCode = mRoot.child("users").child("agent");
        final DatabaseReference checkAgentName = mRoot.child("users").child("customer");
        checkcode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(code)) {

                     uId = dataSnapshot.child(code).child("id").getValue().toString();
                    //Get Name of Agent
                    checkAgentName.child("registered").child("detail").child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot nameDataSnapshot) {
                            if (nameDataSnapshot.hasChild("name")) {
                                createdBy.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                                createdBy.setText("Presentation Created By : " + nameDataSnapshot.child("name").getValue().toString());
                            }
                            else {
                                createdBy.setTextColor(getResources().getColor(R.color.oragnge));
                                createdBy.setText("Presentation Created By : Not Found");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toasty.error(getApplicationContext(),databaseError.getMessage()).show();
                        }
                    });

                    //Get Presentation string
                    checkPresentationCode.child(uId).child("presentation").child(code).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot nameDataSnapshot) {
                            if (nameDataSnapshot.hasChild("string"))
                                presentationString = (nameDataSnapshot.child("string").getValue().toString());
                            if (nameDataSnapshot.hasChild("count"))
                                presentationCount = Integer.parseInt (nameDataSnapshot.child("count").getValue().toString());

                            goForPresentation_bt.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            goForPresentation_bt.setClickable(true);
                            goForPresentation_bt.setEnabled(true);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toasty.error(getApplicationContext(),databaseError.getMessage()).show();
                        }
                    });

                }

                else {
                    createdBy.setText("Code Not found");
                    createdBy.setTextColor(getResources().getColor(R.color.oragnge));
                    goForPresentation_bt.setBackgroundColor(getResources().getColor(R.color.lgray));
                    goForPresentation_bt.setClickable(false);
                    goForPresentation_bt.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(getApplicationContext(),databaseError.getMessage()).show();

            }
        });

    }
}
