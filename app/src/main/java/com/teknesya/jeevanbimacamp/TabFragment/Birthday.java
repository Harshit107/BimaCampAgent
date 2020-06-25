package com.teknesya.jeevanbimacamp.TabFragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.teknesya.jeevanbimacamp.Adapter.CustomerRecyclerAdapter;
import com.teknesya.jeevanbimacamp.Adapter.FreshCallAdapter;
import com.teknesya.jeevanbimacamp.ItemLead;
import com.teknesya.jeevanbimacamp.R;
import com.teknesya.jeevanbimacamp.Utils.DateBima;
import com.teknesya.jeevanbimacamp.model.CustomerlListings;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

import static com.facebook.FacebookSdk.getApplicationContext;

public class BirthDay extends Fragment {
    View progress;
    TextView ptext;

    TextView noFresh;

    String todaysDate;

    private FirebaseAuth mauth;
    private DatabaseReference userReference, groupnameref;
    private ScrollView scrollView;
    //private final ArrayList<ItemLead> messagelist = new ArrayList<>();
    private CustomerRecyclerAdapter todaysFreshAdapter;
    private RecyclerView recyclerView;
    ArrayList <CustomerlListings> listItems = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progress = view.findViewById(R.id.progress_bar);
        ptext = progress.findViewById(R.id.progress_text);
        progress.setVisibility(View.VISIBLE);
        noFresh=view.findViewById(R.id.noFresh);
        todaysDate= DateBima.getTodaysDateMonth();
       // Toasty.info(getContext(),todaysDate).show();
        mauth = FirebaseAuth.getInstance();

        todaysFreshAdapter = new CustomerRecyclerAdapter(listItems,getApplicationContext(),this);
        recyclerView = (RecyclerView) view.findViewById(R.id.freshRecycler);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(todaysFreshAdapter);
        groupnameref = FirebaseDatabase.getInstance().getReference()
                .child("users").child("agent").child(mauth.getUid());


        groupnameref.child("date").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(todaysDate)) {
                    if (!dataSnapshot.child(todaysDate).hasChild("birthday")) {
                        notFound();
                    }
                }else notFound();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//
//        userReference = FirebaseDatabase.getInstance().getReference()
//                .child("users").child("agent").child(mauth.getUid()).child("date");


        groupnameref.keepSynced(true);
        groupnameref.child("date").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String currentDate=dataSnapshot.getKey();
                groupnameref.child("date").child(currentDate).child("birthday").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        if(dataSnapshot.exists()) {

                            final String nodeId = dataSnapshot.getKey();

                            groupnameref.child("customer").child(nodeId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


//                                    String name = dataSnapshot.child("Name").getValue().toString();
//                                    String phone = dataSnapshot.child("Phone").getValue().toString();

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
                                    todaysFreshAdapter.notifyDataSetChanged();




//                                    ItemLead data = new ItemLead(name, phone, nodeId);
//                                    messagelist.add(data);
//                                    todaysFreshAdapter.notifyDataSetChanged();
//                                    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
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
        return inflater.inflate(R.layout.fragment_birthday, container, false);
    }
    public void notFound()
    {
        progress.setVisibility(View.GONE);
        noFresh.setVisibility(View.VISIBLE);
        Toasty.info(getApplicationContext(),"No BirthDay Found Today",Toasty.LENGTH_LONG,true).show();

    }
}
