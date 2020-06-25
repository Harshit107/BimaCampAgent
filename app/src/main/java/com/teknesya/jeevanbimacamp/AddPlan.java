package com.teknesya.jeevanbimacamp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teknesya.jeevanbimacamp.Utils.DateBima;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class AddPlan extends AppCompatActivity {


    FirebaseAuth mAuth;
    DatabaseReference root,getPlan,userData;
    String value,selectedcourse="default",name;
    private ArrayList<String> list_of_array=new ArrayList<>();
    int count=0,studentexist=0;
    Button upload;
    ProgressDialog pbar;
    TextView selectPlan,sumAssured,premium;
    EditText maturity;
  //  int counter =0;
    ImageView premiumDrop,planDrop;
    String dateBirth="",dateMarriage="default";
    DatabaseReference updateImportntDates;
    String planKeyID="";
    int premiumInYear =1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_form2);
        init();
        mAuth =FirebaseAuth.getInstance();
        root= FirebaseDatabase.getInstance().getReference();

        upload=findViewById(R.id.update);
        pbar=new ProgressDialog(this);
        pbar.setMessage("please wait");
        pbar.setCancelable(false);
        pbar.setCanceledOnTouchOutside(false);
        pbar.show();

        updateImportntDates=root.child("users").child("agent")
                .child(mAuth.getUid()).child("date");

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        getPlan=root.child("plan");


        getPlandb();


        premiumDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(AddPlan.this);
                        builder.setTitle("Premium");
                        final String[] s;
                        //Toast.makeText(getApplicationContext(),Month+" "+date,Toast.LENGTH_LONG).show();

                        s = new String[]{

                                "Yearly",
                                "Half-yearly",
                                "quarterly",
                                "Monthly"

                        };
                        final String[] selectedItem = new String[1];
                        int checkedItem = 0;
                        selectedItem[0] = s[0];


                        builder.setSingleChoiceItems(s, checkedItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedItem[0] = s[which];


                                //Toast.makeText(getApplicationContext(),selectedcourse,Toast.LENGTH_LONG).show();
                            }
                        });
                        builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                premium.setText(selectedItem[0]);

                            }
                        });

                        builder.create().show();
                    }

        });

        planDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(AddPlan.this);
                builder.setTitle("Choose a Plan");

// add a radio button list
                final String[] c =new String[list_of_array.size()];
                list_of_array.toArray(c);
                int checkedItem = 0; // cow
                builder.setSingleChoiceItems(c, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedcourse=c[which];
                        Toasty.info(getApplicationContext(),selectedcourse,Toasty.LENGTH_LONG).show();
                    }
                });

// add OK and Cancel buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int p) {
                        selectPlan.setText(selectedcourse);
                        // user clicked OK

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

// create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();



            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                pbar.show();

                if(selectedcourse.equals("default"))
                {
                    Toasty.error(getApplicationContext(),"Please Select Plan",Toasty.LENGTH_LONG,true).show();
                    selectPlan.requestFocus();
                    pbar.dismiss();
                }
                else if(sumAssured.getText().toString().isEmpty())
                {
                    sumAssured.setError("Enter Valid Amount");
                    sumAssured.requestFocus();
                    pbar.dismiss();
                }
                else if(maturity.getText().toString().isEmpty())
                {
                    maturity.setError("Enter Valid Maturity Period");
                    maturity.requestFocus();
                    pbar.dismiss();
                }
                else if(premium.getText().toString().isEmpty())
                {
                    maturity.setError("Select One");
                    maturity.requestFocus();
                    pbar.dismiss();
                }
                else
                {

                    Intent get=getIntent();
                    final String nodeId=get.getStringExtra("nodeId");

                     userData=FirebaseDatabase.getInstance().getReference()
                        .child("users").child("agent")
                        .child(mAuth.getUid()).child("customer").child(String.valueOf(nodeId));


                    userData.child("detail").child("plan").setValue(selectPlan.getText().toString());

                    planKeyID=userData.child("plan").push().getKey();

                    userData.child("plan").child(planKeyID).child("startdate").setValue(DateBima.getDate());

                    userData.child("plan").child(planKeyID).child("plan").setValue(selectPlan.getText().toString());
                    userData.child("plan").child(planKeyID).child("maturity").setValue(maturity.getText().toString());
                    userData.child("plan").child(planKeyID).child("premium").setValue(premium.getText().toString());
                    userData.child("plan").child(planKeyID).child("key").setValue(planKeyID);
                    userData.child("plan").child(planKeyID).child("sumassured").setValue(sumAssured.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            convertPremium(premium.getText().toString());

                            updateMaturity();

                            Intent it=new Intent(getApplicationContext(),AgentMainActivity.class);
                            it.putExtra("nodeId",nodeId);
                            startActivity(it);
                            overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                            pbar.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.error(getApplicationContext(),e.getMessage(),Toasty.LENGTH_LONG,true).show();
                            pbar.dismiss();
                        }
                    });
                } } });



    }

    private void getPlandb() {
        getPlan.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                value=dataSnapshot.getValue(String.class);
                list_of_array.add(value);
                count++;
                pbar.dismiss();
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

    public void init()
    {
        selectPlan=findViewById(R.id.selectPlan);
        sumAssured=findViewById(R.id.sumAssured);
        maturity=findViewById(R.id.maturity);
        premium=findViewById(R.id.premium);
        planDrop=findViewById(R.id.planDrop);
        premiumDrop=findViewById(R.id.premiumDrop);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateMaturity()
    {
        int countMaturity=1;
        int maturityPeriod= (12/premiumInYear) *Integer.parseInt(maturity.getText().toString());
        //int maturityPremium=Integer.parseInt(premium.getText().toString());


        while (countMaturity<=maturityPeriod){
            String date =DateBima.getMaturityDate(countMaturity* premiumInYear);
            userData.child("maturity").child(planKeyID)
                    .child(String.valueOf(countMaturity)).setValue(date);
            countMaturity++;
        }
        Toasty.success(getApplicationContext(),"Success",Toasty.LENGTH_LONG,true).show();




    }
    public void convertPremium(String msg)
    {

        switch (msg)
        {
            case "Yearly":

                premiumInYear =12;
                break;
            case "Half-yearly":
                premiumInYear =6;
                break;
            case "quarterly":
                premiumInYear =3;
                break;
            case "Monthly":
                premiumInYear =1;
                break;


        }


    }

}
