package com.teknesya.jeevanbimacamp.Fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.teknesya.jeevanbimacamp.Adapter.CustomerRecyclerAdapter;
import com.teknesya.jeevanbimacamp.AgentLocationFilter;
import com.teknesya.jeevanbimacamp.R;
import com.teknesya.jeevanbimacamp.UserForm2;
import com.teknesya.jeevanbimacamp.model.CustomerlListings;


import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;


public class AgentCustomerSearchFragment extends Fragment implements CustomerRecyclerAdapter.OnLoadMoreListener {
    private RecyclerView recyclerView;
    private CustomerRecyclerAdapter adapter;
    private List<CustomerlListings> listItems;
    private List<CustomerlListings> subListItems;
    private DatabaseReference mRoot;
    private String mLastKey = "";
    FirebaseAuth mAuth;
    ImageView filter;
    //
//    private SmartMaterialSpinner spProvince;
//    private SmartMaterialSpinner spEmptyItem;
//    private List<String> provinceList;
    //
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
        TextView textview = (TextView) getActivity().findViewById(R.id.tv_name);
        textview.setText("Customer");
         filter = (ImageView) getActivity().findViewById(R.id.filter);
       // filter.setVisibility(View.VISIBLE);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listItems = new ArrayList<>();
        subListItems = new ArrayList<>();
        //  initSpinner();


        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) view.findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
//        mWaveSwipeRefreshLayout.setWaveColor(getResources().getColor(R.color.colorPrimary));


        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refressclose();
                getData();
            }
        });
        //loadingDialog.show();
        mWaveSwipeRefreshLayout.setRefreshing(true);
        refressclose();
        getData();

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertFilter();
            }
        });




    }

    private void getData() {
        listItems = new ArrayList<>();
        //List to add item through database
        Query listingQuery = FirebaseDatabase.getInstance().getReference()
                .child("users").child("agent")
                .child(mAuth.getUid()).child("customer")
                .orderByKey().limitToFirst(10);


        try {  listingQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    if (dataSnapshot.getChildrenCount() > 0) {

                        try {
                            mWaveSwipeRefreshLayout.setRefreshing(false);
                            String name = dataSnapshot.child("detail").child("name").getValue().toString();
                            String email = dataSnapshot.child("detail").child("email").getValue().toString();
                            String plan = dataSnapshot.child("detail").child("plan").getValue().toString();
                            String image = "";
                            try {
                                image = dataSnapshot.child("detail").child("image").getValue().toString();
                            } catch (Exception e) {
                                image = "Default";
                            }

                            String nodeId = dataSnapshot.getKey();
                            CustomerlListings javamesssage = new CustomerlListings(name, image, plan, email, nodeId);
                            listItems.add(javamesssage);
                            mLastKey = dataSnapshot.getKey();
                            setAdapter();
                        } catch (Exception e) {


                        }

                    } else
                        mWaveSwipeRefreshLayout.setRefreshing(false);
    //
    //
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        try {
                            mWaveSwipeRefreshLayout.setEnabled(false);
                            mWaveSwipeRefreshLayout.setRefreshing(false);
                            String name = dataSnapshot.child("detail").child("name").getValue().toString();
                            String email = dataSnapshot.child("detail").child("email").getValue().toString();
                            String plan = dataSnapshot.child("detail").child("gender").getValue().toString();
                            String image = "";
                            try {
                                image = dataSnapshot.child("detail").child("image").getValue().toString();
                            } catch (Exception e) {
                                image = "Default";
                            }

                            String nodeId = dataSnapshot.getKey();
                            CustomerlListings javamesssage = new CustomerlListings(name, image, plan, email, nodeId);
                            listItems.add(javamesssage);
                            mLastKey = dataSnapshot.getKey();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAdapter() {
        mWaveSwipeRefreshLayout.setRefreshing(false);
        adapter = new CustomerRecyclerAdapter(listItems, getContext(), this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoadMore() {
        Log.d("Tag", mLastKey);
        mWaveSwipeRefreshLayout.setEnabled(true);
        mWaveSwipeRefreshLayout.setRefreshing(true);
        getMdata();
    }

    private void getMdata() {
        //List to add item through database
        Query listingQuery = FirebaseDatabase.getInstance().getReference().child("users").child("agent")
                .child(mAuth.getUid()).child("customer")
                .orderByKey().startAt(mLastKey).limitToFirst(21);
        final int[] checker = {1};

        try {
            listingQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    if (dataSnapshot.getChildrenCount() > 0) {
                        mWaveSwipeRefreshLayout.setRefreshing(false);
                        if (checker[0] != 1) {

                            try {
                                String name = dataSnapshot.child("detail").child("name").getValue().toString();
                                String email = dataSnapshot.child("detail").child("email").getValue().toString();
                                String plan = dataSnapshot.child("detail").child("gender").getValue().toString();
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
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else
                            checker[0] = 0;
                    } else
                        mWaveSwipeRefreshLayout.setRefreshing(false);


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    mWaveSwipeRefreshLayout.setRefreshing(false);
                    mWaveSwipeRefreshLayout.setEnabled(false);
                    try {
                        String name = dataSnapshot.child("detail").child("name").getValue().toString();
                        String email = dataSnapshot.child("detail").child("email").getValue().toString();
                        String plan = dataSnapshot.child("detail").child("gender").getValue().toString();
                        String image = "";
                        try {
                            image = dataSnapshot.child("detail").child("image").getValue().toString();
                        } catch (Exception e) {
                            image = "Default";
                        }
                        String nodeId = dataSnapshot.getKey();

                        CustomerlListings javamesssage = new CustomerlListings(name, image, plan, email, nodeId);
                        listItems.add(javamesssage);
                        adapter.notifyDataSetChanged();
                        mLastKey = dataSnapshot.getKey();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                    Toasty.warning(getContext(), databaseError.getMessage(), Toasty.LENGTH_LONG, true).show();
                }
            });
        } catch (Exception e) {


        }
    }

    private void refressclose() {
        DatabaseReference referess = FirebaseDatabase.getInstance().getReference()
                .child("users").child("agent")
                .child(mAuth.getUid());
        referess.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("customer")) {
                    //mWaveSwipeRefreshLayout.setRefreshing(true);
                } else {
                    mWaveSwipeRefreshLayout.setRefreshing(false);
                    Toasty.warning(getContext(), "No Customer Found", Toasty.LENGTH_LONG, true).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showAlertFilter() {


        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Filter");
        final String[] s;
        //Toast.makeText(getApplicationContext(),Month+" "+date,Toast.LENGTH_LONG).show();

        s = new String[]{

                "No Filter",
                "State",
                "Area",
                "Pincode",
                "Landmark",
                "Plan"

        };
        final String[] selectedItem = new String[1];
        int checkedItem = 0;
        selectedItem[0] = s[0];


        builder.setSingleChoiceItems(s, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedItem[0] = s[which];
                selectedItem[0] = selectedItem[0];
                if (which == 0)
                    dialog.dismiss();
                else {

                    Intent it = new Intent(getContext(), AgentLocationFilter.class);
                    it.putExtra("type", selectedItem[0]);
                    getContext().startActivity(it);
                }
            }
        });

        builder.create().show();
    }

    @Override
    public void onResume() {
        super.onResume();
       filter.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        filter.setVisibility(View.GONE);
    }
}



