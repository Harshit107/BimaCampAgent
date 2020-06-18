package com.teknesya.jeevanbimacamp.Fragment;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.teknesya.jeevanbimacamp.Adapter.CustomerRecyclerAdapter;
import com.teknesya.jeevanbimacamp.R;
import com.teknesya.jeevanbimacamp.model.CustomerlListings;


import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;


public class AgentCustomerSearchFragment extends Fragment implements CustomerRecyclerAdapter.OnLoadMoreListener{
    private RecyclerView recyclerView;
    private CustomerRecyclerAdapter adapter;
    private List<CustomerlListings> listItems;
    private List<CustomerlListings> subListItems;
    private DatabaseReference mRoot;
    private String mLastKey="";
    FirebaseAuth mAuth;
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;

    public AgentCustomerSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_agentsearchcustomer, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRoot = FirebaseDatabase.getInstance().getReference();
        mRoot.keepSynced(true);
        TextView textview = (TextView)getActivity().findViewById(R.id.tv_name);
        textview.setText("Customer");
        mAuth=FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listItems = new ArrayList<>();
        subListItems = new ArrayList<>();


        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) view.findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE,Color.WHITE);
//        mWaveSwipeRefreshLayout.setWaveColor(getResources().getColor(R.color.colorPrimary));


        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refressclose();
                // Do work to refresh the list here.
//                final Fragment f=new AgentCustomerSearchFragment();
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.framelayout, f);
//                ft.addToBackStack(null);
//                ft.commit();
                getData();
            }
        });
        //loadingDialog.show();
       mWaveSwipeRefreshLayout.setRefreshing(true);
       refressclose();
       getData();



    }
    private void getData() {
        listItems = new ArrayList<>();
        //List to add item through database
        Query listingQuery = FirebaseDatabase.getInstance().getReference()
                .child("users").child("agent")
                .child(mAuth.getUid()).child("customer")
                .orderByKey().limitToFirst(10);

        listingQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getChildrenCount()>0) {
                    mWaveSwipeRefreshLayout.setRefreshing(false);
                    String name = dataSnapshot.child("detail").child("name").getValue().toString();
                    String email = dataSnapshot.child("detail").child("email").getValue().toString();
                    String plan =  dataSnapshot.child("plan").child("plan").getValue().toString();
                    String image = "";
                    try {
                        image =  dataSnapshot.child("detail").child("image").getValue().toString();
                    } catch (Exception e) {
                        image = "Default";
                    }

                    String nodeId = dataSnapshot.getKey();
                    CustomerlListings javamesssage = new CustomerlListings(name, image, plan, email, nodeId);
                    listItems.add(javamesssage);
                    mLastKey = dataSnapshot.getKey();

                    setAdapter();

                }
                else
                    mWaveSwipeRefreshLayout.setRefreshing(false);
//
//
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getChildrenCount()>0) {
                    mWaveSwipeRefreshLayout.setRefreshing(false);
                    String name =  dataSnapshot.child("detail").child("name").getValue().toString();
                    String email =  dataSnapshot.child("detail").child("email").getValue().toString();
                    String plan =  dataSnapshot.child("detail").child("gender").getValue().toString();
                    String image = "";
                    try {
                        image =  dataSnapshot.child("detail").child("image").getValue().toString();
                    } catch (Exception e) {
                        image = "Default";
                    }

                    String nodeId = dataSnapshot.getKey();
                    CustomerlListings javamesssage = new CustomerlListings(name, image, plan, email, nodeId);
                    listItems.add(javamesssage);
                    mLastKey = dataSnapshot.getKey();
                }else
                    mWaveSwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mWaveSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setAdapter() {
        adapter = new CustomerRecyclerAdapter(listItems, getContext(),this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoadMore() {
        Log.d("Tag",mLastKey);
        mWaveSwipeRefreshLayout.setRefreshing(true);
        getMdata();
    }
    private void getMdata() {
        //List to add item through database
        Query listingQuery = FirebaseDatabase.getInstance().getReference().child("users").child("agent")
                .child(mAuth.getUid()).child("customer")
                .orderByKey().startAt(mLastKey).limitToFirst(21);
        final int[] checker = {1};

        listingQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(dataSnapshot.getChildrenCount()>0) {
                    mWaveSwipeRefreshLayout.setRefreshing(false);
                    if (checker[0] != 1) {

                        String name =  dataSnapshot.child("detail").child("name").getValue().toString();
                        String email =  dataSnapshot.child("detail").child("email").getValue().toString();
                        String plan =  dataSnapshot.child("detail").child("gender").getValue().toString();
                        String image = "";
                        try {
                            image = dataSnapshot.child("image").getValue().toString();
                        } catch (Exception e) {
                            image = "Default";
                        }
                        String nodeId = dataSnapshot.getKey();

                        CustomerlListings javamesssage = new CustomerlListings(name, image, plan, email, nodeId);
                        listItems.add(javamesssage);
                        adapter.notifyDataSetChanged();
                        mLastKey = dataSnapshot.getKey();

                    } else
                        checker[0] = 0;
                }else
                    mWaveSwipeRefreshLayout.setRefreshing(false);


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mWaveSwipeRefreshLayout.setRefreshing(false);
                String name =  dataSnapshot.child("detail").child("name").getValue().toString();
                String email =  dataSnapshot.child("detail").child("email").getValue().toString();
                String plan =  dataSnapshot.child("detail").child("gender").getValue().toString();
                String image = "";
                try {
                    image =  dataSnapshot.child("detail").child("image").getValue().toString();
                } catch (Exception e) {
                    image = "Default";
                }
                String nodeId = dataSnapshot.getKey();

                CustomerlListings javamesssage = new CustomerlListings(name, image, plan, email, nodeId);
                listItems.add(javamesssage);
                adapter.notifyDataSetChanged();
                mLastKey = dataSnapshot.getKey();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mWaveSwipeRefreshLayout.setRefreshing(false);
                Toasty.warning(getContext(), databaseError.getMessage(), Toasty.LENGTH_LONG,true).show();
            }
        });
    }

private void refressclose()
{
    DatabaseReference referess = FirebaseDatabase.getInstance().getReference()
            .child("users").child("agent")
            .child(mAuth.getUid());
    referess.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.hasChild("customer")) {
                mWaveSwipeRefreshLayout.setRefreshing(true);
            }
            else
            {
                mWaveSwipeRefreshLayout.setRefreshing(false);
                Toasty.warning(getContext(),"No Customer Found",Toasty.LENGTH_LONG,true).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}

}
