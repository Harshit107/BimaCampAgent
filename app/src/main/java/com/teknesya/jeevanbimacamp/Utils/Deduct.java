package com.teknesya.jeevanbimacamp.Utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teknesya.jeevanbimacamp.notification.NotificationReminder;

import es.dmoral.toasty.Toasty;

public class Deduct {
    Context context;
    FirebaseAuth mAuth;
    DatabaseReference mRoot,deductPresentationDB,deductamountDB;
    public Deduct(Context context)
    {
        this.context=context;
        mAuth=FirebaseAuth.getInstance();
        mRoot= FirebaseDatabase.getInstance().getReference();

    }

    public void deductPresentation(final int presentatation)
    {
        deductPresentationDB=mRoot.child("users").child("customer").child("registered").child("detail")
                .child(mAuth.getUid());
        deductPresentationDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("presentation"))
                {
                    int prevPresentation=Integer.parseInt(dataSnapshot.child("presentation").getValue().toString());
                    deductPresentationDB.child("presentation").setValue(String.valueOf(prevPresentation-presentatation));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(context,databaseError.getMessage()).show();
            }
        });
    }






}
