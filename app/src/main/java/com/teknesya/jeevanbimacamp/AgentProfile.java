package com.teknesya.jeevanbimacamp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import com.teknesya.jeevanbimacamp.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;

public class AgentProfile extends AppCompatActivity {

    DatePickerDialog picker;

    TextView dob, mariatl_status, anniversary, gender, total_family, account, agent_date, verified_email;
    LinearLayout anniversary_tv;
    EditText name, email, phone;
    CircleImageView profile_pic;
    DatabaseReference dRef, mRoot;
    FirebaseAuth mAuth;
    Button update;

    RelativeLayout initialFocus;
    FirebaseUser firebaseUser;

    StorageReference mStorageRef;
    int PICK_IMAGE=123;
    int checkimage=0;
    String image_upload="";

    ImageView edit;
    Uri imagePath;
    Uri resultUri;
    File actualImage;
    byte[] final_image=null;

    View progress;
    TextView ptext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser=mAuth.getCurrentUser();
        mRoot = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference upload = mStorageRef.child("agent").child("profile_pic").child(mAuth.getUid());
        initilize();
        info();


        ImageButton back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkimage=1;
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });


        if(account.getText().toString().equals("Facebook"))
            verified_email.setVisibility(View.GONE);

        verified_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                ptext.setText("Sending Verification Mail.");
                firebaseUser.sendEmailVerification().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progress.setVisibility(View.GONE);

                        Toasty.error(getApplicationContext(),e.getMessage(),Toasty.LENGTH_LONG,true).show();
                    }
                })
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progress.setVisibility(View.GONE);
                                Toasty.success(getApplicationContext(),"Verification Mail Sent SuccessFul",Toasty.LENGTH_LONG,true).show();
                            }
                        });
            }
        });
        dob.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AgentProfile.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dob.setText("DOB "+dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
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
                picker = new DatePickerDialog(AgentProfile.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                anniversary.setText("Anniversary "+dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });



        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


        final AlertDialog.Builder builder = new AlertDialog.Builder(AgentProfile.this);
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
        selectedItem[0]=s[0];


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


                final AlertDialog.Builder builder = new AlertDialog.Builder(AgentProfile.this);
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
                selectedItem[0]=s[0];


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

                        total_family.setText("Total Family Member "+selectedItem[0]);

                    }
                });

                builder.create().show();
            }
        });



        mariatl_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final AlertDialog.Builder builder = new AlertDialog.Builder(AgentProfile.this);
                builder.setTitle("Anniversary");
                final String[] s;
                //Toast.makeText(getApplicationContext(),Month+" "+date,Toast.LENGTH_LONG).show();

                s = new String[]{

                        "Married",
                        "Un-Married",

                };

                final String[] selectedItem = new String[1];
                int checkedItem = 0;
                selectedItem[0]=s[0];


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
                        if(selectedItem[0].equals("Un-Married"))
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


                if(email.getText().toString().isEmpty())
                {
                    email.setError("Enter Email id");
                    email.requestFocus();
                }
                else if(phone.getText().length()<10)
                {
                    phone.setError("Phone Number Must be 10 digits");
                    phone.requestFocus();
                }
                else if(name.getText().toString().isEmpty())
                {
                    name.setError("Enter name First");
                    name.requestFocus();
                }
                else if(gender.getText().toString().equals("Gender"))
                {
                    Toasty.error(getApplicationContext(),"Select Gender First",Toasty.LENGTH_LONG,true).show();
                }
                else if(dob.getText().toString().equals("Date of Birth"))
                {
                    Toasty.error(getApplicationContext(),"Enter Your Birthday",Toasty.LENGTH_LONG,true).show();
                }
                else if(mariatl_status.getText().toString().equals("Marital Status"))
                {
                    Toasty.error(getApplicationContext(),"Select Marital Status First",Toasty.LENGTH_LONG,true).show();
                }

                else
                {
                    progress.setVisibility(View.VISIBLE);
                    if(checkimage==1&& final_image!=null) {

                        UploadTask uploadTask = (UploadTask) upload.child("profile_pic").putBytes(final_image);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progress.setVisibility(View.GONE);
                                Toasty.error(getApplicationContext(), e.getMessage(), Toasty.LENGTH_SHORT,true).show();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = 100.0 *(double) (taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                               // int currentprogress = (int) progress;
                                ptext.setText("Upload is " + progress + "% done");
                            }
                        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                                ptext.setText("Paused Uploading..");
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                if (taskSnapshot.getMetadata() != null) {
                                    if (taskSnapshot.getMetadata().getReference() != null) {

                                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {

                                                dRef.child("name").setValue(name.getText().toString());
                                                dRef.child("email").setValue(email.getText().toString());
                                                dRef.child("phone").setValue(phone.getText().toString());
                                                dRef.child("maritalstatus").setValue(mariatl_status.getText().toString());
                                                dRef.child("anniversarydate").setValue(anniversary.getText().toString());
                                                dRef.child("dob").setValue(dob.getText().toString());
                                                dRef.child("image").setValue(uri.toString());
                                                dRef.child("gender").setValue(gender.getText().toString());
                                                dRef.child("totalfamily").setValue(total_family.getText().toString())



                                                //update
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                progress.setVisibility(View.GONE);
                                                                Toasty.success(getApplicationContext(),"Profile update Successful",Toasty.LENGTH_LONG,true).show();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        ptext.setVisibility(View.GONE);
                                                        Toasty.error(getApplicationContext(),e.getMessage(),Toasty.LENGTH_LONG,true).show();

                                                    }
                                                });

                                            }
                                        });
                                    }
                                }
                            }
                        });

                    }
                    else
                    {
                        dRef.child("name").setValue(name.getText().toString());
                        dRef.child("email").setValue(email.getText().toString());
                        dRef.child("phone").setValue(phone.getText().toString());
                        dRef.child("maritalstatus").setValue(mariatl_status.getText().toString());
                        dRef.child("anniversarydate").setValue(anniversary.getText().toString());
                        dRef.child("dob").setValue(dob.getText().toString());
                        dRef.child("gender").setValue(gender.getText().toString());
                        dRef.child("totalfamily").setValue(total_family.getText().toString())
                        //update
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progress.setVisibility(View.GONE);
                                Toasty.success(getApplicationContext(),"Profile update Successful",Toasty.LENGTH_LONG,true).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progress.setVisibility(View.VISIBLE);
                                Toasty.error(getApplicationContext(),e.getMessage(),Toasty.LENGTH_LONG,true).show();

                            }
                        });

                    }

                }


            }
        });






    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        checkimage=1;
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null) {
            //      String imagedata=data.getDataString();
            imagePath = data.getData();
            CropImage.activity(imagePath)
                    .setAspectRatio(1,1)
                    .start(AgentProfile.this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult resultcp = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = resultcp.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = resultcp.getError();
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }


            try{actualImage=new File(resultUri.getPath());}catch (Exception e){
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();}
            if(actualImage!=null){
                try {
                    Bitmap compressedImage = new Compressor(this)
                            .setMaxWidth(250)
                            .setMaxHeight(250)
                            .setQuality(50)
                            .compressToBitmap(actualImage)
                            ;
                    ByteArrayOutputStream baos=new ByteArrayOutputStream();
                    compressedImage.compress(Bitmap.CompressFormat.JPEG,50,baos);
                    final_image=baos.toByteArray();
                    checkimage=1;
                    profile_pic.setImageURI(resultUri);
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }}
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initilize() {

        progress = findViewById(R.id.progress_bar);
        ptext = progress.findViewById(R.id.progress_text);
        progress.setVisibility(View.GONE);
        dob = findViewById(R.id.dob);
        mariatl_status = findViewById(R.id.marital_status);
        anniversary_tv = findViewById(R.id.anniversary_tv);
        anniversary = findViewById(R.id.anniversary_date);
        gender = findViewById(R.id.gender);
        edit=findViewById(R.id.edit);
        initialFocus=findViewById(R.id.main_layout);
        total_family = findViewById(R.id.totalfamily);
        account = findViewById(R.id.account);
        agent_date = findViewById(R.id.agent_date);
        verified_email = findViewById(R.id.verified_tv);
        name = findViewById(R.id.name);
        update=findViewById(R.id.update);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        profile_pic = findViewById(R.id.profile_pic);

    }

    public void info() {
        dRef = mRoot.child("users").child("customer").child("registered")
                .child("detail").child(mAuth.getUid());



        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("name"))
                    name.setText(dataSnapshot.child("name").getValue().toString());
                if (dataSnapshot.hasChild("email"))
                    email.setText(dataSnapshot.child("email").getValue().toString());

                else{
                    email.setEnabled(true);
                    verified_email.setVisibility(View.GONE);
                }
                if (dataSnapshot.hasChild("account"))
                    account.setText(dataSnapshot.child("account").getValue().toString());
                if (dataSnapshot.hasChild("phone")) {
                    phone.setText(dataSnapshot.child("phone").getValue().toString());
                    phone.setEnabled(false);
                } else
                    phone.setEnabled(true);

                if (dataSnapshot.hasChild("creationdate")) {
                    agent_date.setText(dataSnapshot.child("creationdate").getValue().toString());
                    agent_date.setEnabled(false);
                } else
                    agent_date.setEnabled(true);


                if (dataSnapshot.hasChild("totalfamily")) {
                    String tf = dataSnapshot.child("totalfamily").getValue().toString();
                    total_family.setText(tf);
                    total_family.setEnabled(true);
                }

                if (dataSnapshot.hasChild("gender")) {
                    String gende=dataSnapshot.child("gender").getValue().toString();
                    gender.setText(gende);
                    gender.setEnabled(false);
                } else
                    gender.setEnabled(true);

                if (dataSnapshot.hasChild("maritalstatus")) {
                    String ms=dataSnapshot.child("maritalstatus").getValue().toString();
                    mariatl_status.setText(ms);
                    if(mariatl_status.getText().toString().equals("Married"))
                        anniversary_tv.setVisibility(View.VISIBLE);
                    else
                        anniversary_tv.setVisibility(View.GONE);
                    mariatl_status.setEnabled(false);
                } else
                    mariatl_status.setEnabled(true);

                if (dataSnapshot.hasChild("anniversarydate")) {
                    String md=dataSnapshot.child("anniversarydate").getValue().toString();
                    anniversary.setText(md);
                    anniversary.setEnabled(false);
                } else
                    anniversary.setEnabled(true);

                if (dataSnapshot.hasChild("dob")) {
                    String dobb=dataSnapshot.child("dob").getValue().toString();
                    dob.setText(dobb);
                    dob.setEnabled(false);
                } else
                    dob.setEnabled(true);
                if (dataSnapshot.hasChild("image")) {
                    Picasso.get()
                            .load(dataSnapshot.child("image").getValue().toString())
                            .placeholder(R.drawable.avtar)
                            .into(profile_pic);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(getApplicationContext(),databaseError.getMessage(),Toasty.LENGTH_LONG,true).show();
            }
        });
        if(firebaseUser.isEmailVerified()) {
            verified_email.setText("Your Email is Verified");
            verified_email.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }
        else if(account.getText().toString().equals("Facebook"))
            verified_email.setVisibility(View.GONE);
        else
        {

            verified_email.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            verified_email.setText("Click Here to Verify Your Email ");
        }
        initialFocus.setFocusable(true);
        initialFocus.setFocusableInTouchMode(true);

    }



}
