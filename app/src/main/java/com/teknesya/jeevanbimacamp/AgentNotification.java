package com.teknesya.jeevanbimacamp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teknesya.jeevanbimacamp.Adapter.ChatMessageAdapter;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class AgentNotification extends AppCompatActivity {
    Button send, image;
    private FirebaseAuth mauth;
    private DatabaseReference userreference, groupnameref, groupmessagekeyref;
    private ScrollView scrollView;
    private final List<javamessage> messagelist = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private ChatMessageAdapter chatMessageAdapter;
    private RecyclerView usermessageList;
    boolean p=false;
    ProgressDialog pbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_notification);
        ImageView back = (ImageView) findViewById(R.id.back);
        startProgreeBar(true);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        mauth = FirebaseAuth.getInstance();

        chatMessageAdapter = new ChatMessageAdapter(messagelist,getApplicationContext());
        usermessageList = (RecyclerView) findViewById(R.id.privae_message_list_of_user);
        linearLayoutManager = new LinearLayoutManager(this);
        usermessageList.setLayoutManager(linearLayoutManager);
        usermessageList.setAdapter(chatMessageAdapter);
        pbar = new ProgressDialog(this);
        groupnameref = FirebaseDatabase.getInstance().getReference()
                .child("users").child("agent").child(mauth.getUid()).child("notification");

        groupnameref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()<1) {
                    startProgreeBar(false);
                    Toasty.warning(getApplicationContext(),"No Notification Found",Toasty.LENGTH_LONG,true).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.warning(getApplicationContext(),databaseError.getMessage(),Toasty.LENGTH_LONG,true).show();

            }
        });

        groupnameref.keepSynced(true);
        groupnameref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    // displaymessage(dataSnapshot);
                    javamessage message = dataSnapshot.getValue(javamessage.class);
                    messagelist.add(message);
                    chatMessageAdapter.notifyDataSetChanged();
                    usermessageList.smoothScrollToPosition(usermessageList.getAdapter().getItemCount());
                    startProgreeBar(false);

                }



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                javamessage message = dataSnapshot.getValue(javamessage.class);
                messagelist.add(message);
                chatMessageAdapter.notifyDataSetChanged();
                usermessageList.smoothScrollToPosition(usermessageList.getAdapter().getItemCount());
                startProgreeBar(false);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void startProgreeBar(Boolean check)
    {
        View progress = findViewById(R.id.progress_bar);
        TextView ptext = progress.findViewById(R.id.progress_text);
        if(check) {

            progress.setVisibility(View.VISIBLE);
            ptext.setText("Please Wait..");
        }
        else
        {
            progress.setVisibility(View.GONE);
        }
    }
}
