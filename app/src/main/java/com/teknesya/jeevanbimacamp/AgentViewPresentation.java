package com.teknesya.jeevanbimacamp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teknesya.jeevanbimacamp.Adapter.PresentationRecyclerAdapter;
import com.teknesya.jeevanbimacamp.Adapter.ReferralRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AgentViewPresentation extends AppCompatActivity {
    View progress;
    TextView ptext;

    TextView nosuccess;



    private FirebaseAuth mauth;
    private DatabaseReference userreference, groupnameref, groupmessagekeyref;
    private ScrollView scrollView;
    private final List<presentationG> messagelist = new ArrayList<>();
    private PresentationRecyclerAdapter successAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_view_presentation);


         progress = findViewById(R.id.progress_bar);
        ptext = progress.findViewById(R.id.progress_text);
        progress.setVisibility(View.VISIBLE);
        nosuccess=findViewById(R.id.noSuccess);


        mauth = FirebaseAuth.getInstance();

        successAdapter = new PresentationRecyclerAdapter(messagelist,getApplicationContext(),AgentViewPresentation.this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerSuccess);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(successAdapter);
        groupnameref = FirebaseDatabase.getInstance().getReference()
                .child("users").child("agent").child(mauth.getUid()).child("presentation");

        groupnameref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()==0) {
                    progress.setVisibility(View.GONE);
                    nosuccess.setVisibility(View.VISIBLE);
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

                    String name= null;
                    String nodeID= null;
                    String remaning= null;
                    String string= null;
                    String date= null;
                    try {
                        name = dataSnapshot.child("name").getValue().toString();
                        nodeID = dataSnapshot.child("nodeid").getValue().toString();
                        remaning = dataSnapshot.child("count").getValue().toString();
                        string = dataSnapshot.child("string").getValue().toString();
                        Log.d("presentationA",string);
                        date = dataSnapshot.child("date").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    presentationG data = new presentationG(name,date,nodeID,remaning,string);
                    messagelist.add(data);
                    successAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
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
