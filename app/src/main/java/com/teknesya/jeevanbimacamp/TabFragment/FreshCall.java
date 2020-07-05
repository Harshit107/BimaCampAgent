package com.teknesya.jeevanbimacamp.TabFragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.teknesya.jeevanbimacamp.Adapter.FreshCallAdapter;
import com.teknesya.jeevanbimacamp.AgentMainActivity;
import com.teknesya.jeevanbimacamp.ItemLead;
import com.teknesya.jeevanbimacamp.R;
import com.teknesya.jeevanbimacamp.Utils.DateBima;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FreshCall extends Fragment {
    private View progress;
    private TextView ptext;

    private TextView noFresh;

    private String todaysDate;

    private FirebaseAuth mauth;
    private DatabaseReference userReference, groupnameref;
    private ScrollView scrollView;
    private final ArrayList<ItemLead> messagelist = new ArrayList<>();
    private FreshCallAdapter todaysFreshAdapter;
    private RecyclerView recyclerView;
    LinearLayout freshCallLinear;
    boolean isFound = false;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progress = view.findViewById(R.id.progress_bar);
        ptext = progress.findViewById(R.id.progress_text);
        progress.setVisibility(View.VISIBLE);
        noFresh = view.findViewById(R.id.noFresh);
        todaysDate = DateBima.getTodaysDateMonth();
        // Toasty.info(getContext(),todaysDate).show();
        mauth = FirebaseAuth.getInstance();


        todaysFreshAdapter = new FreshCallAdapter(messagelist, getApplicationContext(), getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.freshRecycler);

        freshCallLinear = view.findViewById(R.id.freshcallLinear);
        freshCallLinear.setVisibility(View.INVISIBLE);

        TextView textview = (TextView) getActivity().findViewById(R.id.tv_name);
        textview.setText("Fresh Call");


        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(todaysFreshAdapter);

        groupnameref = FirebaseDatabase.getInstance().getReference()
                .child("users").child("agent").child(mauth.getUid()).child("date");


        groupnameref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(DateBima.getTodaysDateMonth())) {

                    Log.d("Fresh", "Found Date");

                    if (dataSnapshot.child(DateBima.getTodaysDateMonth()).hasChild("lead")) {
                        Log.d("Fresh", "Found Lead ");
                        foundLead();
                    } else {
                        notFoundLead();
                        Log.d("Fresh", "Not Found Lead");
                    }
                } else {
                    notFoundLead();
                    Log.d("Fresh", "Not Found Date");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        groupnameref.keepSynced(true);

        groupnameref.child(DateBima.getTodaysDateMonth()).child("lead").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    Log.d("Fresh", "At Child");
                    String nodeId = dataSnapshot.getKey();
                    String name = dataSnapshot.child("Name").getValue().toString();
                    String phone = dataSnapshot.child("Phone").getValue().toString();
                    ItemLead data = new ItemLead(name, phone, nodeId);
                    messagelist.add(data);
                    todaysFreshAdapter.notifyDataSetChanged();
                   // recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());

                }

//
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
        return inflater.inflate(R.layout.fragment_fresh_call, container, false);
    }

    public void foundLead() {
        recyclerView.setVisibility(View.VISIBLE);
        freshCallLinear.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        noFresh.setVisibility(View.GONE);
    }

    public void notFoundLead() {
        freshCallLinear.setVisibility(View.GONE);
        progress.setVisibility(View.GONE);
        noFresh.setVisibility(View.VISIBLE);
    }


}
