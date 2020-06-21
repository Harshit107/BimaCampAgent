package com.teknesya.jeevanbimacamp;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;

public class UserForm extends AppCompatActivity {

    DatePickerDialog picker;

    TextView dob, mariatl_status, anniversary, gender, total_family;
    LinearLayout anniversary_tv;
    EditText name, email, phone, state, landmark, area, pincode;
    DatabaseReference dRef, mRoot;
    FirebaseAuth mAuth;
    Button update;

    RelativeLayout initialFocus;
    FirebaseUser firebaseUser;
    String dB,dM="default";
//
//    View progress;
//    TextView ptext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_form);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        mRoot = FirebaseDatabase.getInstance().getReference();
        initilize();


        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//
        dob.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                // date picker dialog

                picker = new DatePickerDialog(UserForm.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dB=dayOfMonth + "-" + (monthOfYear + 1) ;
                                dob.setText("DOB " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        anniversary.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(UserForm.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dM=dayOfMonth + "-" + (monthOfYear + 1);
                                anniversary.setText("Anniversary " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final AlertDialog.Builder builder = new AlertDialog.Builder(UserForm.this);
                builder.setTitle("Select Gender");
                final String[] s;
                //Toast.makeText(getApplicationContext(),Month+" "+date,Toast.LENGTH_LONG).show();

                s = new String[]{
                        "Male",
                        "Female",
                        "Other"

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
                        gender.setText(selectedItem[0]);
                    }
                });
                builder.create().show();
            }
        });

        total_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final AlertDialog.Builder builder = new AlertDialog.Builder(UserForm.this);
                builder.setTitle("Family Member");
                final String[] s;
                //Toast.makeText(getApplicationContext(),Month+" "+date,Toast.LENGTH_LONG).show();

                s = new String[]{
                        "1",
                        "2",
                        "3",
                        "4",
                        "5",
                        "6",
                        "7",
                        "8",
                        "9",
                        "10"

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

                        total_family.setText("Total Family Member " + selectedItem[0]);

                    }
                });

                builder.create().show();
            }
        });


        mariatl_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(UserForm.this);
                builder.setTitle("Anniversary");
                final String[] s;
                //Toast.makeText(getApplicationContext(),Month+" "+date,Toast.LENGTH_LONG).show();

                s = new String[]{

                        "Married",
                        "Un-Married",

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

                        mariatl_status.setText(selectedItem[0]);
                        if (selectedItem[0].equals("Un-Married"))
                            anniversary_tv.setVisibility(View.GONE);
                        else
                            anniversary_tv.setVisibility(View.VISIBLE);
                    }
                });

                builder.create().show();
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (email.getText().toString().isEmpty()) {
                    email.setError("Enter Email id");
                    email.requestFocus();
                } else if (phone.getText().length() < 10) {
                    phone.setError("Phone Number Must be 10 digits");
                    phone.requestFocus();
                } else if (name.getText().toString().isEmpty()) {
                    name.setError("Enter Name First");
                    name.requestFocus();
                } else if (gender.getText().toString().equals("Gender")) {
                    Toasty.error(getApplicationContext(), "Select Gender First", Toasty.LENGTH_LONG, true).show();
                } else if (dob.getText().toString().isEmpty()) {
                    Toasty.error(getApplicationContext(), "Enter Your Birthday", Toasty.LENGTH_LONG, true).show();
                } else if (mariatl_status.getText().toString().isEmpty()) {
                    Toasty.error(getApplicationContext(), "Select Marital Status First", Toasty.LENGTH_LONG, true).show();
                } else {
                    String cName = "";
                    cName = name.getText().toString();
                    String cEmail = "";
                    cEmail = email.getText().toString();
                    String cMaritalStatus = "";
                    cMaritalStatus = mariatl_status.getText().toString();
                    String cGender = "";
                    cGender = gender.getText().toString();
                    String cDob = "";
                    cDob = dob.getText().toString();
                    String cPhone = "";
                    cPhone = phone.getText().toString();
                    String cAnni = "";
                    cAnni = anniversary.getText().toString();
                    String cTotalFamily = "";
                    cTotalFamily = total_family.getText().toString();
                    String cState = "";
                    cState = state.getText().toString();
                    String cArea = "";
                    cArea = area.getText().toString();
                    String cPincode = "";
                    cPincode = pincode.getText().toString();
                    String cLandMark = "";
                    cLandMark = landmark.getText().toString();


                    Intent it=new Intent(getApplicationContext(),UserForm2.class);
                    it.putExtra("name",cName);
                    it.putExtra("email",cEmail);
                    it.putExtra("phone",cPhone);
                    it.putExtra("dB",dB);
                    it.putExtra("dM",dM);
                    it.putExtra("totalFamily",cTotalFamily);
                    it.putExtra("state",cState);
                    it.putExtra("area",cArea);
                    it.putExtra("pincode",cPincode);
                    it.putExtra("landMark",cLandMark);
                    it.putExtra("maritalStatus",cMaritalStatus);
                    it.putExtra("gender",cGender);
                    it.putExtra("dob",cDob);
                    it.putExtra("anni",cAnni);
                    startActivity(it);

                }


            }
        });


    }


    private void initilize() {

//        progress = findViewById(R.id.progress_bar);
//        ptext = progress.findViewById(R.id.progress_text);
//        progress.setVisibility(View.GONE);

        dob = findViewById(R.id.dob);
        mariatl_status = findViewById(R.id.marital_status);
        anniversary_tv = findViewById(R.id.anniversary_tv);
        anniversary = findViewById(R.id.anniversary_date);
        gender = findViewById(R.id.gender_tv);
        initialFocus = findViewById(R.id.main_layout);
        total_family = findViewById(R.id.totalfamily);
        name = findViewById(R.id.name);
        update = findViewById(R.id.update);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        state = findViewById(R.id.state);
        area = findViewById(R.id.area);
        landmark = findViewById(R.id.landmark);
        pincode = findViewById(R.id.pincode);


    }


}
