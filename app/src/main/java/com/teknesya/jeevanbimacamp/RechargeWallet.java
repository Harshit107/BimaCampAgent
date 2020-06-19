package com.teknesya.jeevanbimacamp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teknesya.jeevanbimacamp.Utils.MailSender;
import com.teknesya.jeevanbimacamp.notification.NotificationReminder;

import es.dmoral.toasty.Toasty;

public class RechargeWallet extends AppCompatActivity {

    View progress;
    TextView pText;
    EditText amount;
    Button addAmount;
    FirebaseAuth mAuth;
    String walletPrevAmount;
    DatabaseReference mRoot,updateAmount;
    String msg="Your Wallet has been Recharge Successful",title="Wallet Recharge Successful",eMail="";
    String prevwalletMessage="\nYour Previous Wallet Balance was : ";
    String currentWalletMessage="\nYour Current Wallet Balance is : ";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup_balance);
        mRoot= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        getAmountFromDatabase();
        progress=findViewById(R.id.progress_bar);
        pText=progress.findViewById(R.id.progress_text);
        progress.setVisibility(View.GONE);
        amount=findViewById(R.id.amount);
        addAmount=findViewById(R.id.add_amountBt);
        ImageButton back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amoubtToAdd=amount.getText().toString();
                if( validateAmount(amoubtToAdd)){
                    succesPayment(amoubtToAdd);

                }


            }
        });
    }

    public void succesPayment(String amoubtToAdd)
    {
        progress.setVisibility(View.VISIBLE);
        getAmountFromDatabase();
        updateAmountToDatabase(amoubtToAdd);

    }

    private void updateAmountToDatabase(final String amoubtToAdd) {
        final int amountToBeAdded=(Integer.parseInt(amoubtToAdd)+Integer.parseInt(walletPrevAmount));
        updateAmount=mRoot.child("users").child("customer")
                .child("registered").child("detail").child(mAuth.getUid());
        updateAmount.child("wallet").setValue(amountToBeAdded)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pText.setText("Success");
                        NotificationReminder notificationReminder=new NotificationReminder(getApplicationContext()
                        ,"Recharge Success",
                                "Thankyou for Using JeevanBimacamp\nYour Recharge has been successful");
                        String nMsg=msg+prevwalletMessage+walletPrevAmount+currentWalletMessage+amountToBeAdded;
                        MailSender mailSender=new MailSender(RechargeWallet.this,nMsg,title,eMail);
                        mailSender.sendEmail();
                  Toasty.success(getApplicationContext(),"Recharge Successful",Toasty.LENGTH_LONG,true).show();
                        Intent it=new Intent(getApplicationContext(), BuyPresentation.class);
                        progress.setVisibility(View.GONE);
                        finish();
                        startActivity(it);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.setVisibility(View.GONE);
                Toasty.error(getApplicationContext(),e.getMessage(),Toasty.LENGTH_LONG,true).show();
            }
        });

    }

    private boolean validateAmount(String amoubtToAdd) {
        if(amoubtToAdd.isEmpty()||Integer.parseInt(amoubtToAdd)<10 || amoubtToAdd.length()>5)
        {
            amount.setError("Enter Valid amount");
            return false;
        }
        return true;
    }


    public void getAmountFromDatabase()
    {
        DatabaseReference getPrevAmount=mRoot.child("users").child("customer")
              .child("registered").child("detail").child(mAuth.getUid());
        getPrevAmount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("email"))
                    eMail=dataSnapshot.child("email").getValue().toString();
                if(dataSnapshot.hasChild("wallet"))
                    walletPrevAmount=dataSnapshot.child("wallet").getValue().toString();
                else
                    walletPrevAmount="0";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.info(getApplicationContext(),databaseError.getMessage(),Toasty.LENGTH_LONG,true).show();
            }
        });
    }

}
