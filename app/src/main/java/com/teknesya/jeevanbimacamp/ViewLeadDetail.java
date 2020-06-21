package com.teknesya.jeevanbimacamp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teknesya.jeevanbimacamp.Adapter.LeadViewDetailAdapter;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class ViewLeadDetail extends AppCompatActivity {
    View progress;
    TextView ptext;

    TextView noLeade;
    DatePickerDialog picker;
    private boolean fabExpanded = false;
    private FloatingActionButton fabSettings;
    private LinearLayout layoutSchedule;
    private LinearLayout layoutUpdate;
    private LinearLayout layoutAdd;
    private LinearLayout layoutDelete;
    Animation slideUp;
    Animation slideDown;
    String date="";




    private FirebaseAuth mauth;
    private DatabaseReference userreference, groupnameref, groupmessagekeyref,mRoot,updateImportntDates;
    private final ArrayList<ItemLead> messagelist = new ArrayList<>();
    private LeadViewDetailAdapter successAdapter;
    private RecyclerView recyclerView;
    String nodeID="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lead_detail);

        Intent get=getIntent();
        nodeID=get.getStringExtra("nodeId");


        progress = findViewById(R.id.progress_bar);
        ptext = progress.findViewById(R.id.progress_text);
        progress.setVisibility(View.VISIBLE);
        noLeade = findViewById(R.id.noLead);
        mauth = FirebaseAuth.getInstance();
        //Toasty.error(getApplicationContext(),"hey").show();

        LinearLayout layoutFabSettings = (LinearLayout) this.findViewById(R.id.layoutFabSettings);
        fabSettings = (FloatingActionButton) this.findViewById(R.id.fabSetting);
        layoutSchedule = (LinearLayout) this.findViewById(R.id.layoutSchedule);
        layoutUpdate = (LinearLayout) this.findViewById(R.id.layoutupdate);
        layoutDelete = (LinearLayout) this.findViewById(R.id.delete);
        layoutAdd = (LinearLayout) this.findViewById(R.id.layoutadd);

        mRoot=FirebaseDatabase.getInstance().getReference();
        updateImportntDates=mRoot.child("users").child("agent")
                .child(mauth.getUid()).child("date");

         slideUp = AnimationUtils.loadAnimation(this, R.anim.push_up_in);
        slideDown = AnimationUtils.loadAnimation(this, R.anim.fadeout);

       // setupSlider();
        //closeSubMenusFab();




        successAdapter = new LeadViewDetailAdapter(messagelist, getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerSuccess);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(successAdapter);
        groupnameref = FirebaseDatabase.getInstance().getReference()
                .child("users").child("agent").child(mauth.getUid()).child("lead").child(nodeID);

        groupnameref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    progress.setVisibility(View.GONE);
                    noLeade.setVisibility(View.VISIBLE);
                    Toasty.warning(getApplicationContext(), "No Lead Found", Toasty.LENGTH_LONG, true).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.warning(getApplicationContext(), databaseError.getMessage(), Toasty.LENGTH_LONG, true).show();

            }
        });

        groupnameref.keepSynced(true);
        groupnameref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {

                    String key = dataSnapshot.getKey();
                    String value = dataSnapshot.getValue().toString();

                    ItemLead il = new ItemLead(key, value);
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





        layoutSchedule.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                // date picker dialog
                picker = new DatePickerDialog(ViewLeadDetail.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date=dayOfMonth + "-" + (monthOfYear + 1) ;
                                updateImportntDates.child(date).child("lead").child(nodeID).setValue(nodeID)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toasty.success(getApplicationContext(),"Scheduled Successful").show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toasty.error(getApplicationContext(),e.getMessage()).show();

                                    }
                                });

                                //updateLead.child(messageKeyID).updateChildren(hashMap);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        layoutUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(getApplicationContext(),UpdateLeadDetail.class);
                it.putExtra("nodeId",nodeID);
                startActivity(it);
            }
        });

        layoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   groupnameref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           Toasty.success(getApplicationContext(),"Lead Removed Successfully").show();
                           finish();
                       }
                   }) .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Toasty.error(getApplicationContext(),e.getMessage()).show();

                       }
                   });

            }
        });
        layoutAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(getApplicationContext(),UserForm.class);
                startActivity(it);
            }
        });

        //When main Fab (Settings) is clicked, it expands if not expanded already.
        //Collapses if main FAB was open already.
        //This gives FAB (Settings) open/close behavior
        fabSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded) {
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            }
        });

        //Only main FAB is visible in the beginning

    }       //closes FAB submenus

    private void closeSubMenusFab() {
      //  Toasty.error(getApplicationContext(),"success").show();
        layoutSchedule.setVisibility(View.INVISIBLE);
        layoutUpdate.setVisibility(View.INVISIBLE);
        layoutDelete.setVisibility(View.INVISIBLE);
        layoutAdd.setVisibility(View.INVISIBLE);
        layoutUpdate.startAnimation(slideDown);
        layoutSchedule.startAnimation(slideDown);
        layoutDelete.startAnimation(slideDown);
        layoutAdd.startAnimation(slideDown);
        fabSettings.setImageResource(R.drawable.ic_add_black_24dp);
        fabExpanded = false;

    }

    //Opens FAB submenus
    private void openSubMenusFab() {
       // Toasty.success(getApplicationContext(),"success").show();
        layoutSchedule.setVisibility(View.VISIBLE);
        layoutAdd.setVisibility(View.VISIBLE);
        layoutUpdate.setVisibility(View.VISIBLE);
        layoutDelete.setVisibility(View.VISIBLE);
        //Change settings icon to 'X' icon
        layoutUpdate.startAnimation(slideUp);
        layoutSchedule.startAnimation(slideUp);
        layoutDelete.startAnimation(slideUp);
        layoutAdd.startAnimation(slideUp);

        fabSettings.setImageResource(R.drawable.ic_close_black_24dp);
        fabExpanded = true;
    }
}
