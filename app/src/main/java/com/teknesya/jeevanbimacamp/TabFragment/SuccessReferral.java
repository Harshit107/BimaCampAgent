package com.teknesya.jeevanbimacamp.TabFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teknesya.jeevanbimacamp.Adapter.ReferralRecyclerAdapter;
import com.teknesya.jeevanbimacamp.R;
import com.teknesya.jeevanbimacamp.UserProfileData;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SuccessReferral extends Fragment {
    View progress;
    TextView ptext;

    TextView nosuccess;



    private FirebaseAuth mauth;
    private DatabaseReference userreference, groupnameref, groupmessagekeyref;
    private ScrollView scrollView;
    private final List<UserProfileData> messagelist = new ArrayList<>();
    private ReferralRecyclerAdapter successAdapter;
    private RecyclerView recyclerView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progress = view.findViewById(R.id.progress_bar);
        ptext = progress.findViewById(R.id.progress_text);
        progress.setVisibility(View.VISIBLE);
        nosuccess=view.findViewById(R.id.noSuccess);


        mauth = FirebaseAuth.getInstance();

        successAdapter = new ReferralRecyclerAdapter(messagelist,getApplicationContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerSuccess);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(successAdapter);
        groupnameref = FirebaseDatabase.getInstance().getReference()
                .child("users").child("customer").child("registered").child("referral").child("success");

        groupnameref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(mauth.getUid())) {
                    progress.setVisibility(View.GONE);
                    nosuccess.setVisibility(View.VISIBLE);
                    Toasty.warning(getApplicationContext(),"No Success Referral Found",Toasty.LENGTH_LONG,true).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.warning(getApplicationContext(),databaseError.getMessage(),Toasty.LENGTH_LONG,true).show();

            }
        });

        groupnameref.child(mauth.getUid()).keepSynced(true);
        groupnameref.child(mauth.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    // displaymessage(dataSnapshot);
                    String userId=dataSnapshot.getKey().toString();
                    DatabaseReference getUserData=FirebaseDatabase.getInstance().getReference()
                            .child("users").child("customer").child("registered").child("detail")
                            .child(userId);
                    getUserData.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot kdataSnapshot) {
                            UserProfileData data = kdataSnapshot.getValue(UserProfileData.class);
                            messagelist.add(data);
                            successAdapter.notifyDataSetChanged();
                            recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
                            progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });




                }



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_success_referral, container, false);
    }
}
