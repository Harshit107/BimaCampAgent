package com.teknesya.jeevanbimacamp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teknesya.jeevanbimacamp.Adapter.LeadCreateAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class CreateNewLead extends AppCompatActivity {

    RecyclerView recyclerView;
    Button addDratilBt;

    DatePickerDialog picker;

    ImageView addLeadToDatabaseBt;
    EditText keyE, valueE;
    HashMap<String, Object> hashMap = new HashMap<>();
    FirebaseAuth mAuth;
    String date = "default";
    DatabaseReference mRoot, updateLead,updateImportntDates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_lea);

        keyE = findViewById(R.id.key);
        valueE = findViewById(R.id.value);
        addDratilBt = findViewById(R.id.addDetail);
        addLeadToDatabaseBt = findViewById(R.id.add_lead_db);
        mAuth = FirebaseAuth.getInstance();
        mRoot = FirebaseDatabase.getInstance().getReference();
        updateLead = mRoot.child("users").child("agent").child(mAuth.getUid()).child("lead");
        updateImportntDates=mRoot.child("users").child("agent")
                .child(mAuth.getUid()).child("date");
        final String messageKeyID = updateLead.push().getKey();


        // Initializing list view with the custom adapter
        final ArrayList<ItemLead> itemList = new ArrayList<ItemLead>();
        final LeadCreateAdapter itemArrayAdapter = new LeadCreateAdapter( itemList);
        recyclerView = (RecyclerView) findViewById(R.id.item_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemArrayAdapter);

        addLeadToDatabaseBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hashMap.isEmpty()) {
                    Toasty.error(getApplicationContext(), "Enter Detail of Lead").show();

                }
                if(!hashMap.containsKey("Name") )
                {
                    Toasty.error(getApplicationContext(),"Please Enter Name Of Lead").show();
                    new AlertDialog.Builder(CreateNewLead.this)
                            .setMessage("Lead Name is Must.\nUse Title as Name to save Name of Lead")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }).create().show();
                }
               else if(!hashMap.containsKey("Phone") )
                {
                    Toasty.error(getApplicationContext(),"Please Enter phone Of Lead").show();
                    new AlertDialog.Builder(CreateNewLead.this)
                            .setMessage("Lead Phone Number is Must.\nUse Title as Phone to save Phone Number of Lead")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }).create().show();
                }
                else {


                    AlertDialog.Builder ask=new AlertDialog.Builder(CreateNewLead.this);
                    ask.setMessage("Do You want To  Schedule Date For Reminder");
                    ask.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            final Calendar cldr = Calendar.getInstance();
                            int day = cldr.get(Calendar.DAY_OF_MONTH);
                            int month = cldr.get(Calendar.MONTH);
                            int year = cldr.get(Calendar.YEAR);

                            // date picker dialog
                            picker = new DatePickerDialog(CreateNewLead.this,
                                    new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                            date=dayOfMonth + "-" + (monthOfYear + 1) ;
                                            updateImportntDates.child(date).child("lead")
                                                    .child(messageKeyID).updateChildren(hashMap);
                                            updateLead.child(messageKeyID).updateChildren(hashMap);
                                            Toasty.success(getApplicationContext(),"Success").show();
                                            finish();
                                        }
                                    }, year, month, day);
                            picker.show();

////

                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            updateLead.child(messageKeyID).updateChildren(hashMap);
                            Toasty.success(getApplicationContext(),"Success").show();
                            finish();
                        }
                    });
                    ask.create().show();





                }

//
            }
        });

        addDratilBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = keyE.getText().toString();
                String value = valueE.getText().toString();
                if (key.isEmpty()) {
                    keyE.setError("Enter Title");
                    return;
                }
                if (value.isEmpty()) {
                    valueE.setError("Enter Value");
                    return;
                }
                itemList.add(new ItemLead(key, value));
                hashMap.put(key, value);
                keyE.setText("");
                valueE.setText("");
                itemArrayAdapter.notifyDataSetChanged();


            }
        });

//        // Populating list items
//        for(int i=0; i<10; i++) {
//            itemList.add(new ItemLead("1","2"));
//        }

    }
}
