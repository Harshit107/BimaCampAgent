package com.teknesya.jeevanbimacamp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.teknesya.jeevanbimacamp.Adapter.AgentCustomerPlanAdapter;
import com.teknesya.jeevanbimacamp.Adapter.MaturityRecycleAdapter;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;

public class AgentCustomerDetail extends AppCompatActivity {

    TextView dob, mariatl_status, anniversary, gender, total_family,totalPlan,pincode,state,landmark,area;
    TextView name, email, phone;
    DatabaseReference dRef, mRoot;
    FirebaseAuth mAuth;
    RelativeLayout plPlanRecycle;
    LinearLayout anniversary_tv;

    CircleImageView profile_pic;
    StorageReference mStorageRef;
    int PICK_IMAGE=123;
    int checkimage=0;
    String image_upload="";

    Button uploadImage;
    Uri imagePath;
    Uri resultUri;
    File actualImage;
    byte[] final_image=null;

    ImageView edit;

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

        mStorageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference upload = mStorageRef.child("customer").child(mAuth.getUid()).child(nodeId);


        initilize();
        updatePersonalDetail();

        ImageView back=findViewById(R.id.back);
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

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkimage==1&& final_image!=null) {
                    progress.setVisibility(View.VISIBLE);
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

                                            dRef.child(nodeId).child("detail").child("image").setValue(uri.toString())

                                                    //update
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            progress.setVisibility(View.GONE);
                                                            Toasty.success(getApplicationContext(),"Profile Picture update Successful",Toasty.LENGTH_LONG,true).show();
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
                    Toasty.error(getApplicationContext(),"Image Not Selected ").show();

            }
        });

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
        pincode = findViewById(R.id.pincode);
        landmark = findViewById(R.id.landmark);
        area = findViewById(R.id.area);
        state = findViewById(R.id.state);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        anniversary_tv = findViewById(R.id.anniversary_tv);
        phone = findViewById(R.id.phone);
        addPlan=findViewById(R.id.add);
        edit=findViewById(R.id.edit);
        planRecycle=findViewById(R.id.recycler_customer_plan_detail);
        uploadImage=findViewById(R.id.updateImage);

        profile_pic = findViewById(R.id.profile_pic);
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

                if (dataSnapshot.hasChild("anniversary")) {
                    String md=dataSnapshot.child("anniversary").getValue().toString();
                    anniversary.setText(md);
                }

                if (dataSnapshot.hasChild("dob")) {
                    String dobb=dataSnapshot.child("dob").getValue().toString();
                    dob.setText(dobb);}

                if (dataSnapshot.hasChild("state")) {
                    String states=dataSnapshot.child("state").getValue().toString();
                    state.setText(states);}
                if (dataSnapshot.hasChild("area")) {
                    String areaa=dataSnapshot.child("area").getValue().toString();
                    area.setText(areaa);}
                if (dataSnapshot.hasChild("pincode")) {
                    String pincodee=dataSnapshot.child("pincode").getValue().toString();
                    pincode.setText(pincodee);}
                if (dataSnapshot.hasChild("landmark")) {
                    String landmarkk=dataSnapshot.child("landmark").getValue().toString();
                    landmark.setText(landmarkk);}
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        checkimage=1;
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null) {
            //      String imagedata=data.getDataString();
            imagePath = data.getData();
            CropImage.activity(imagePath)
                    .setAspectRatio(1,1)
                    .start(AgentCustomerDetail.this);
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



}
