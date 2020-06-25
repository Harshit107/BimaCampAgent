package com.teknesya.jeevanbimacamp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfWriter;
import com.teknesya.jeevanbimacamp.Utils.DateBima;
import com.teknesya.jeevanbimacamp.Utils.Deduct;

import com.itextpdf.text.Document;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class AgentCreatePresentation extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mRoot,checkUnique;
    String presentationUniqueIdl;
    String total_present="0";
   static String name="name",string="One reason may be that apples contain soluble fiber — the kind that can help lower your blood cholesterol levels.--\n" +
            "=> " +
            "They also contain polyphenols, which have antioxidant effects. Many of these are concentrated in the peel.--\n" +
            "=> " +
            "One of these polyphenols is the flavonoid epicatechin, which may lower blood pressure.--\n" +
            "=> " +
            "An analysis of studies found that high intakes of flavonoids were linked to a 20% lower risk of stroke (6Trusted Source).--\n" +
            "=> " +
            "Flavonoids can help prevent heart disease by lowering blood pressure, reducing “bad” LDL oxidation, and acting as antioxidants (7Trusted Source).--\n" +
            "=> " +
            "Another study comparing the effects of eating an apple a day to taking statins — a class of drugs known to lower cholesterol — concluded that apples would be almost as effective at reducing death from heart disease as the drugs (8Trusted Source).--\n" +
            "=> " +
            "However, since this was not a controlled trial, findings must be taken with a grain of salt.--\n" +
            "=> " +
            "Another study linked consuming white-fleshed fruits and vegetables, such as apples and pears, to a reduced risk of stroke. For every 25 grams — \nabout 1/5 cup of apple slices — consumed, the risk of stroke decreased by 9% (9Trusted Source).";


    View progress;
    TextView ptext;

    Boolean checkerPresentation=false;
    Button create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_create_presentation);

        create =findViewById(R.id.create);
        mAuth=FirebaseAuth.getInstance();
        mRoot= FirebaseDatabase.getInstance().getReference();
        getTotalPresent();
        progress = findViewById(R.id.progress_bar);
        ptext = progress.findViewById(R.id.progress_text);

        string+=string;


//        create.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (checkerPresentation) {
//                    progress.setVisibility(View.VISIBLE);
//                    checkUniqueDB();
//                } else {
//                    Toasty.error(getApplicationContext(),"Insufficient Presentation").show();
//                  AlertDialog.Builder builder=  new AlertDialog.Builder(AgentCreatePresentation.this);
//                           builder.setPositiveButton("Buy Now ", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    startActivity(new Intent(AgentCreatePresentation.this, BuyPresentation.class));
//                                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//                                }
//                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.cancel();
//                        }
//                    })
//                           .setMessage("Insufficient Presentation\nBuy Presentation Starting At Rs 1/- Only")
//                           .setCancelable(false);
//                    try {
//                        builder  .create().show();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//            }
//        });




        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pdf1Activity.verifyStoragePermissions(AgentCreatePresentation.this);
//                if(isStoragePermissionGranted()) {
//                    new CreatePdf(getApplicationContext())
//                            .setPdfName("FirstPdf")
//                            .openPrintDialog(false)
//                            .setContent("hi\nhello")
//                            .setContentBaseUrl(null)
//                            .setPageSize(PrintAttributes.MediaSize.ISO_A4)
//                            .setContent(string)
//                            .setContent("hi\nhello")
//                            .setFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyPdf")
//                            .setCallbackListener(new CreatePdf.PdfCallbackListener() {
//                                @Override
//                                public void onFailure(@NotNull String s) {
//                                    // handle error
//                                    Toasty.error(getApplicationContext(),s).show();
//                                }
//
//                                @Override
//                                public void onSuccess(@NotNull String s) {
//                                    // do your stuff here
//                                    Toasty.success(getApplicationContext(),s).show();
//                                }
//                            })
//                            .create();
//                }
            }
        });

    }



    /* *************************  Presentation Method   ***************************/

    public void checkUniqueDB()
    {
        checkUnique=mRoot.child("unique").child("presentation");
        checkUnique.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(presentationUniqueIdl)) {
                    presentationUniqueIdl = getRandomNumberString();
                    checkUniqueDB();

                }
                else
                {
                    createPresentationLink();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createPresentationLink() {
        checkUnique.child(presentationUniqueIdl).child("id").setValue(mAuth.getUid());
        DatabaseReference updatePresentationstring=mRoot.child("users").child("agent").child(mAuth.getUid())
                .child("presentation").child(presentationUniqueIdl);
        updatePresentationstring.child("count").setValue("2");
        updatePresentationstring.child("name").setValue(name);
        updatePresentationstring.child("nodeid").setValue(presentationUniqueIdl);
        updatePresentationstring.child("share").setValue("false");
        updatePresentationstring.child("string").setValue(string);
        updatePresentationstring.child("date").setValue(DateBima.getDate())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progress.setVisibility(View.GONE);
                        Deduct d=new Deduct(getApplicationContext());
                        d.deductPresentation(1);
                        Toasty.success(getApplicationContext(),"Presentation Created successfully",Toasty.LENGTH_LONG,true).show();
                        startActivity(new Intent(AgentCreatePresentation.this, AgentViewPresentation.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                });
    }

    public  String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(9999);
        String sid = UUID.randomUUID().toString();

        // this will convert any number sequence into 6 character.
        return (total_present+sid.substring(2,6)+String.format("%06d", number));
    }


    public  void getTotalPresent()
    {
        DatabaseReference getPresentDB=mRoot.child("agent").child(mAuth.getUid())
                .child("presentation");
        getPresentDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                total_present=String.valueOf(dataSnapshot.getChildrenCount());
                // Toasty.info(getApplicationContext(),total_present).show();
                presentationUniqueIdl=getRandomNumberString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(getApplicationContext(),databaseError.getMessage()).show();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        DatabaseReference mRoot=FirebaseDatabase.getInstance().getReference();
        final DatabaseReference getPresentationDB = mRoot.child("users").child("customer")
                .child("registered").child("detail").child(mAuth.getUid());


        getPresentationDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("presentation")) {
                    checkerPresentation = !dataSnapshot.child("presentation").getValue().toString().equals("0");

                } else {
                    getPresentationDB.child("presentation").setValue("0");
                    checkerPresentation = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(getApplicationContext(), databaseError.getMessage()).show();
            }
        });

    }



}
