package com.teknesya.jeevanbimacamp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teknesya.jeevanbimacamp.Adapter.CustomeSearchrRecyclerAdapter;
import com.teknesya.jeevanbimacamp.Adapter.CustomerRecyclerAdapter;
import com.teknesya.jeevanbimacamp.model.CustomerlListings;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AgentLocationFilter extends AppCompatActivity {

    private SmartMaterialSpinner<String> spProvince;
    private SmartMaterialSpinner spEmptyItem;
    private ArrayList<String> provinceList;

    //Recycler
    private CustomeSearchrRecyclerAdapter filterLocationdapter;
    private RecyclerView FilterRecyclerView;
    ArrayList<CustomerlListings> listItems = new ArrayList<>();


    String type;
    ProgressDialog pbar;
    TextView searchFilter;
    String nodeId = "";

    FirebaseAuth mAuth;
    DatabaseReference mRoot, groupnameref,fiterKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_location_filter);

        Intent it = getIntent();
        type = it.getStringExtra("type");

        searchFilter=findViewById(R.id.filter_tv);
        searchFilter.setText(type);
        mAuth = FirebaseAuth.getInstance();
        mRoot = FirebaseDatabase.getInstance().getReference();
        pbar = new ProgressDialog(this);
        pbar.setMessage("Please Wait");
        pbar.setCanceledOnTouchOutside(false);
        pbar.show();


        filterLocationdapter = new CustomeSearchrRecyclerAdapter(listItems, getApplicationContext());
        FilterRecyclerView = (RecyclerView) findViewById(R.id.filter_recyclear);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        FilterRecyclerView.setLayoutManager(layoutManager);
        FilterRecyclerView.setAdapter(filterLocationdapter);
        groupnameref = mRoot
                .child("users").child("agent").child(mAuth.getUid());
        Log.d("filterr","Before init");
        
        ImageView back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initSpinner();




    }

    private void initSpinner() {
        spProvince = findViewById(R.id.spinner1);
        provinceList = new ArrayList<>();
         fiterKey = mRoot.child("users").child("agent").child(mAuth.getUid()).child("filter").child(type);


        fiterKey.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0)
                    pbar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        fiterKey.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    Log.d("filterr",dataSnapshot.getKey());
                    provinceList.add(dataSnapshot.getKey());
                    pbar.dismiss();
                    spProvince.setItem(provinceList);
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

        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // nodeId = provinceList.get(position);
                Log.d("filterr","At Item Selected");

                pbar.show();
                pbar.setMessage("Gathering Information..");
                setFilterRecyclerView(provinceList.get(position));
                Toasty.info(AgentLocationFilter.this, provinceList.get(position), Toast.LENGTH_SHORT, false).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toasty.info(AgentLocationFilter.this, "No Search", Toast.LENGTH_SHORT, false).show();

            }
        });


    }

    public void setFilterRecyclerView(String filterResult) {


        fiterKey.child(filterResult).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                //Getting Every node present
                Log.d("filterr","At Filter Result");


                nodeId=dataSnapshot.getKey();

                groupnameref.child("customer").child(nodeId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                        filterLocationdapter.notifyDataSetChanged();
                        pbar.dismiss();

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


}
