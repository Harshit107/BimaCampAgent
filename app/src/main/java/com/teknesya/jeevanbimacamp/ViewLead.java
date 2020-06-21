package com.teknesya.jeevanbimacamp;

import android.os.Bundle;
import android.view.View;
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
import com.teknesya.jeevanbimacamp.Adapter.LeadCreateAdapter;
import com.teknesya.jeevanbimacamp.Adapter.LeadViewAdapter;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class ViewLead extends AppCompatActivity {
    View progress;
    TextView ptext;

    TextView noLeade;



    private FirebaseAuth mauth;
    private DatabaseReference userreference, groupnameref, groupmessagekeyref;
    private ScrollView scrollView;
    private final ArrayList<ItemLead> messagelist = new ArrayList<>();
    private LeadViewAdapter successAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lead);


         progress = findViewById(R.id.progress_bar);
        ptext = progress.findViewById(R.id.progress_text);
        progress.setVisibility(View.VISIBLE);
        noLeade=findViewById(R.id.noLead);


        mauth = FirebaseAuth.getInstance();

        successAdapter = new LeadViewAdapter(messagelist,getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerSuccess);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(successAdapter);
        groupnameref = FirebaseDatabase.getInstance().getReference()
                .child("users").child("agent").child(mauth.getUid()).child("lead");

        groupnameref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()==0) {
                    progress.setVisibility(View.GONE);
                    noLeade.setVisibility(View.VISIBLE);
                    Toasty.warning(getApplicationContext(),"No Presentation Found",Toasty.LENGTH_LONG,true).show();

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

                    String name=dataSnapshot.child("Name").getValue().toString();
                    String phone=dataSnapshot.child("Phone").getValue().toString();
                    String nodeID=dataSnapshot.getKey();

                    ItemLead il=new ItemLead(name,phone,nodeID);
                    messagelist.add(il);
                    successAdapter.notifyDataSetChanged();
                    //recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
                    progress.setVisibility(View.GONE);


                }



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    successAdapter.notifyDataSetChanged();
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

}
