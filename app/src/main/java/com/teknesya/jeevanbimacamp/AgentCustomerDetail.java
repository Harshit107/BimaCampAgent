package com.teknesya.jeevanbimacamp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.teknesya.jeevanbimacamp.Adapter.AgentCustomerPlanAdapter;
import com.teknesya.jeevanbimacamp.Adapter.MaturityRecycleAdapter;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class AgentCustomerDetail extends AppCompatActivity {

    TextView dob, mariatl_status, anniversary, gender, total_family,totalPlan;
    TextView name, email, phone;
    DatabaseReference dRef, mRoot;
    FirebaseAuth mAuth;
    RelativeLayout plPlanRecycle;
    LinearLayout anniversary_tv;
    Button addPlan;
    String nodeId;
    RecyclerView planRecycle;
    //RecyclerView maturityrecycle;
    AgentCustomerPlanAdapter agentCustomerPlanAdapter;
    //MaturityRecycleAdapter maturityRecycleAdapter;
    private final ArrayList<PlanRecyclearData> planRecyclearArrayList = new ArrayList<>();
   // private final ArrayList<ItemLead> maturityRecyclearArrayList = new ArrayList<>();


    View progress;
    TextView ptext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_customer_detail);
        mAuth = FirebaseAuth.getInstance();
        mRoot = FirebaseDatabase.getInstance().getReference();
        final Intent get=getIntent();
        nodeId=get.getStringExtra("nodeId");

        initilize();
        updatePersonalDetail();

//        ImageButton back=findViewById(R.id.back);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });


        //Plan Recycle
        agentCustomerPlanAdapter = new AgentCustomerPlanAdapter(planRecyclearArrayList,getApplicationContext());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        planRecycle.setLayoutManager(layoutManager);
        planRecycle.setAdapter(agentCustomerPlanAdapter);
        updatePlan();

        //maturity recycle

//        maturityRecycleAdapter = new MaturityRecycleAdapter(maturityRecyclearArrayList,getApplicationContext());
//        maturityrecycle.setLayoutManager(layoutManager);
//        maturityrecycle.setAdapter(maturityRecycleAdapter);

        addPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(getApplicationContext(),AddPlan.class);
                it.putExtra("nodeId",nodeId);
                startActivity(it);
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
            }
        });


    }


    private void initilize() {

        progress = findViewById(R.id.progress_bar);
        ptext = progress.findViewById(R.id.progress_text);
        progress.setVisibility(View.GONE);
        dob = findViewById(R.id.dob);
        mariatl_status = findViewById(R.id.status);
        anniversary = findViewById(R.id.anni);
        totalPlan=findViewById(R.id.totalplan);
        gender = findViewById(R.id.gender);
        total_family = findViewById(R.id.totalfamily);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        anniversary_tv = findViewById(R.id.anniversary_tv);
        phone = findViewById(R.id.phone);
        addPlan=findViewById(R.id.add);
        planRecycle=findViewById(R.id.recycler_customer_plan_detail);
       // maturityrecycle=findViewById(R.id.recycler_customer_plan_maturity_detail);

    }

    public void updatePersonalDetail() {
        dRef = mRoot.child("users").child("agent").child(mAuth.getUid())
                .child("customer");

        dRef.child(nodeId).child("plan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalPlan.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        dRef.child(nodeId).child("detail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("name"))
                    name.setText(dataSnapshot.child("name").getValue().toString());
                if (dataSnapshot.hasChild("email"))
                    email.setText(dataSnapshot.child("email").getValue().toString());
                if (dataSnapshot.hasChild("phone"))
                    phone.setText(dataSnapshot.child("phone").getValue().toString());

                if (dataSnapshot.hasChild("totalfamily")) {
                    String tf = dataSnapshot.child("totalfamily").getValue().toString();
                    total_family.setText(tf);
                }

                if (dataSnapshot.hasChild("gender")) {
                    String gende = dataSnapshot.child("gender").getValue().toString();
                    gender.setText(gende);
                }

                if (dataSnapshot.hasChild("maritalstatus")) {
                    String ms=dataSnapshot.child("maritalstatus").getValue().toString();
                    mariatl_status.setText(ms);
                    if(mariatl_status.getText().toString().equals("Married"))
                        anniversary_tv.setVisibility(View.VISIBLE);
                    else
                        anniversary_tv.setVisibility(View.GONE);

                }

                if (dataSnapshot.hasChild("anniversarydate")) {
                    String md=dataSnapshot.child("anniversarydate").getValue().toString();
                    anniversary.setText(md);
                }

                if (dataSnapshot.hasChild("dob")) {
                    String dobb=dataSnapshot.child("dob").getValue().toString();
                    dob.setText(dobb);}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(getApplicationContext(),databaseError.getMessage(),Toasty.LENGTH_LONG,true).show();
            }
        });


    }



    public void updatePlan()
    {
        dRef.child(nodeId).child("plan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                PlanRecyclearData planRecyclear=dataSnapshot.getValue(PlanRecyclearData.class);
                planRecyclearArrayList.add(planRecyclear);
                agentCustomerPlanAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                PlanRecyclearData planRecyclear=dataSnapshot.getValue(PlanRecyclearData.class);
                planRecyclearArrayList.add(planRecyclear);
                agentCustomerPlanAdapter.notifyDataSetChanged();
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

//    public void updateMaturity()
//    {
//        dRef.child(nodeId).child("plan").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                PlanRecyclearData planRecyclear=dataSnapshot.getValue(PlanRecyclearData.class);
//                planRecyclearArrayList.add(planRecyclear);
//                agentCustomerPlanAdapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                PlanRecyclearData planRecyclear=dataSnapshot.getValue(PlanRecyclearData.class);
//                planRecyclearArrayList.add(planRecyclear);
//                agentCustomerPlanAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//
//    }




}
