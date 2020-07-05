package com.teknesya.jeevanbimacamp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teknesya.jeevanbimacamp.Utils.DateBima;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class UserForm2 extends AppCompatActivity {

    DatePickerDialog picker;

    FirebaseAuth mAuth;
    DatabaseReference root, getPlan, userData;
    String value, selectedPlan = "Select", name;
    private ArrayList<String> list_of_array = new ArrayList<>();
    int count = 0, studentexist = 0;
    Button upload;
    ProgressDialog pbar;
    TextView selectPlan, sumAssured, premium;
    EditText maturity;
    //  int counter =0;
    ImageView premiumDrop, planDrop;
    String dateBirth = "", dateMarriage = "default";
    DatabaseReference updateImportntDates;
    String messageKey = "", planKeyID = "";
    int premiumInYear = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_form2);
        init();
        mAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance().getReference();

        final Intent get = getIntent();


        String cName = "";
        cName = get.getStringExtra("name");
        String cEmail = "";
        cEmail = get.getStringExtra("email");
        String cMaritalStatus = "";
        cMaritalStatus = get.getStringExtra("maritalStatus");
        String cGender = "";
        cGender = get.getStringExtra("gender");
        String cDob = "";
        cDob = get.getStringExtra("dob");
        String cPhone = "";
        cPhone = get.getStringExtra("phone");
        String cAnni = "";
        cAnni = get.getStringExtra("anni");

        Log.d("anni", cAnni);

        String cTotalFamily = "";
        cTotalFamily = get.getStringExtra("totalFamily");
        String cState = "";
        cState = get.getStringExtra("state");
        String cArea = "";
        cArea = get.getStringExtra("area");
        String cPincode = "";
        cPincode = get.getStringExtra("pincode");
        String cLandMark = "";
        cLandMark = get.getStringExtra("landMark");

        dateBirth = get.getStringExtra("dB");
        dateMarriage = get.getStringExtra("dM");

        upload = findViewById(R.id.update);
        pbar = new ProgressDialog(this);
        pbar.setMessage("please wait");
        pbar.setCancelable(false);
        pbar.setCanceledOnTouchOutside(false);
        pbar.show();

        updateImportntDates = root.child("users").child("agent")
                .child(mAuth.getUid());

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getPlan = root.child("plan");


        getPlandb();


        premiumDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(UserForm2.this);
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

                final AlertDialog.Builder builder = new AlertDialog.Builder(UserForm2.this);
                builder.setTitle("Choose a Plan");

// add a radio button list
                final String[] c = new String[list_of_array.size()];
                list_of_array.toArray(c);
                int checkedItem = 0; // cow
                builder.setSingleChoiceItems(c, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedPlan = c[which];
                        Toasty.info(getApplicationContext(), selectedPlan, Toasty.LENGTH_LONG).show();
                    }
                });

// add OK and Cancel buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int p) {
                        selectPlan.setText(selectedPlan);
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


        //conversion

        final String finalCName = cName;
        final String finalCEmail = cEmail;
        final String finalCPhone = cPhone;
        final String finalCGender = cGender;
        final String finalCMaritalStatus = cMaritalStatus;
        final String finalCAnni = cAnni;
        final String finalCTotalFamily = cTotalFamily;
        final String finalCDob = cDob;
        final String finalCState = cState;
        final String finalCArea = cArea;
        final String finalCLandMark = cLandMark;
        final String finalCPincode = cPincode;


        upload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                pbar.show();

                if (selectedPlan.equals("Selected")) {
                    Toasty.error(getApplicationContext(), "Please Select Plan", Toasty.LENGTH_LONG, true).show();
                    selectPlan.requestFocus();
                    pbar.dismiss();
                } else if (sumAssured.getText().toString().isEmpty()) {
                    sumAssured.setError("Enter Valid Amount");
                    sumAssured.requestFocus();
                    pbar.dismiss();
                } else if (maturity.getText().toString().isEmpty()) {
                    maturity.setError("Enter Valid Maturity Period");
                    maturity.requestFocus();
                    pbar.dismiss();
                } else if (premium.getText().toString().isEmpty()) {
                    maturity.setError("Select One");
                    maturity.requestFocus();
                    pbar.dismiss();
                } else {
                    //conversion


                    DatabaseReference countCustomer = FirebaseDatabase.getInstance().getReference()
                            .child("users").child("agent")
                            .child(mAuth.getUid()).child("customer");
                    messageKey = countCustomer.push().getKey();

                    userData = FirebaseDatabase.getInstance().getReference()
                            .child("users").child("agent")
                            .child(mAuth.getUid()).child("customer").child(String.valueOf(messageKey));


                    if (!dateMarriage.equals("default"))
                        updateImportntDates.child("date").child(dateMarriage).child("marriage").child(String.valueOf(messageKey)).setValue(finalCName);
                    if (!dateBirth.equals("default"))
                        updateImportntDates.child("date").child(dateBirth).child("birthday").child(String.valueOf(messageKey)).setValue(finalCName);

                    //Location

                    if (!finalCState.equals(""))
                        updateImportntDates.child("filter").child("State").child(finalCState).child(messageKey).setValue(finalCState);

                    if (!finalCArea.equals(""))
                        updateImportntDates.child("filter").child("Area").child(finalCArea).child(messageKey).setValue(finalCArea);

                    if (!finalCPincode.equals(""))
                        updateImportntDates.child("filter").child("Pincode").child(finalCPincode).child(messageKey).setValue(finalCPincode);

                    if (!finalCLandMark.equals(""))
                        updateImportntDates.child("filter").child("Landmark").child(finalCLandMark).child(messageKey).setValue(finalCLandMark);


                    updateImportntDates.child("filter").child("Plan").child(selectPlan.getText().toString()).child(messageKey).setValue(DateBima.getDate());

                    Log.d("anni", finalCAnni);
                    Log.d("anni", dateMarriage);

                    userData.child("detail").child("anniversary").setValue(finalCAnni);
                    userData.child("detail").child("name").setValue(finalCName);
                    userData.child("detail").child("email").setValue(finalCEmail);
                    userData.child("detail").child("phone").setValue(finalCPhone);
                    userData.child("detail").child("gender").setValue(finalCGender);
                    userData.child("detail").child("maritalstatus").setValue(finalCMaritalStatus);
                    userData.child("detail").child("anniversary").setValue(finalCAnni);
                    userData.child("detail").child("totalfamily").setValue(finalCTotalFamily);
                    userData.child("detail").child("dob").setValue(finalCDob);
                    userData.child("detail").child("state").setValue(finalCState);
                    userData.child("detail").child("area").setValue(finalCArea);
                    userData.child("detail").child("landmark").setValue(finalCLandMark);
                    userData.child("detail").child("pincode").setValue(finalCPincode);
                    userData.child("detail").child("plan").setValue(selectPlan.getText().toString());


                    //planDetail
                    planKeyID = userData.child("plan").push().getKey();
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

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toasty.error(getApplicationContext(), e.getMessage(), Toasty.LENGTH_LONG, true).show();
                                    pbar.dismiss();
                                }
                            });
                }
            }
        });


    }

    private void getPlandb() {
        getPlan.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                value = dataSnapshot.getValue(String.class);
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

    public void init() {
        selectPlan = findViewById(R.id.selectPlan);
        sumAssured = findViewById(R.id.sumAssured);
        maturity = findViewById(R.id.maturity);
        premium = findViewById(R.id.premium);
        planDrop = findViewById(R.id.planDrop);
        premiumDrop = findViewById(R.id.premiumDrop);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateMaturity() {

        DatabaseReference updatePremiumDate = FirebaseDatabase.getInstance().getReference()
                .child("users").child("agent")
                .child(mAuth.getUid()).child("premium");

        int countMaturity = 1;
        long maturityPeriod = (12 / premiumInYear) * Integer.parseInt(maturity.getText().toString());
        //int maturityPremium=Integer.parseInt(premium.getText().toString());

        HashMap<String, Object> hashMap = new HashMap<>();

        while (countMaturity <= maturityPeriod) {
            String date = DateBima.getMaturityDate(countMaturity * premiumInYear);
            hashMap.put(String.valueOf(countMaturity), date);

            updatePremiumDate.child(date).child(planKeyID).setValue(messageKey);
            countMaturity++;
        }

        Log.d("maturity", hashMap.toString());
        userData.child("maturity").child(planKeyID).updateChildren(hashMap).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(getApplicationContext(), "Maturity error : " + e.getMessage()).show();
                startActivity(new Intent(getApplicationContext(), AgentMainActivity.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                pbar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toasty.success(getApplicationContext(), "Success", Toasty.LENGTH_LONG, true).show();
                startActivity(new Intent(getApplicationContext(), AgentMainActivity.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                pbar.dismiss();
            }
        });


    }

    public void convertPremium(String msg) {

        switch (msg) {
            case "Yearly":

                premiumInYear = 12;
                break;
            case "Half-yearly":
                premiumInYear = 6;
                break;
            case "quarterly":
                premiumInYear = 3;
                break;
            case "Monthly":
                premiumInYear = 1;
                break;


        }


    }



    /* ********************  Update Premium Date ********************/

    public void uploadPremium() {


    }

}
