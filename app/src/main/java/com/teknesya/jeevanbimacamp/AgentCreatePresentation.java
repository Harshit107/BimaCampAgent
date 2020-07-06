package com.teknesya.jeevanbimacamp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.teknesya.jeevanbimacamp.Utils.DateBima;
import com.teknesya.jeevanbimacamp.Utils.Deduct;

import com.itextpdf.text.Document;
import com.teknesya.jeevanbimacamp.notification.NotificationReminder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AgentCreatePresentation extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mRoot, checkUnique;
    String presentationUniqueIdl;
    String total_present = "0";
    int PdfAmount = 1;


    //PDF Creation
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 25, Font.BOLD,BaseColor.BLUE);
   // private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL);
   // private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    String msg, PdfName = "JB-" + DateBima.getDate() + "-"+String.valueOf(Calendar.getInstance().getTimeInMillis()%10000) + ".pdf";


    static String name = "name", string = "One reason may be that apples contain soluble fiber — the kind that can help lower your blood cholesterol levels.--\n" +
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

    Boolean checkerPresentation = false;
    Button createPresentation, createPdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_create_presentation);

        createPresentation = findViewById(R.id.create);
        createPdf = findViewById(R.id.create_pdf);
        mAuth = FirebaseAuth.getInstance();
        mRoot = FirebaseDatabase.getInstance().getReference();
        getTotalPresent();
        progress = findViewById(R.id.progress_bar);
        ptext = progress.findViewById(R.id.progress_text);
        string += string;


        createPresentation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkerPresentation) {
                    progress.setVisibility(View.VISIBLE);
                    ptext.setText("Creating Please Wait");
                    checkUniqueDB();
                } else {
                    Toasty.error(getApplicationContext(), "Insufficient Presentation").show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(AgentCreatePresentation.this);
                    builder.setPositiveButton("Buy Now ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(AgentCreatePresentation.this, BuyPresentation.class));
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })
                            .setMessage("Insufficient Presentation\nBuy Presentation Starting At Rs 1/- Only")
                            .setCancelable(false);
                    try {
                        builder.create().show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        });


        createPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.info(getApplicationContext(),"Please Wait !",Toasty.LENGTH_LONG,false).show();
                getAmountFromDatabase();
            }
        });

    }















    /* **********************************************************/
    /* **********************************************************/

    /*           PDF Section

    /* **********************************************************/
    /* **********************************************************/


    public void showAlertTorecharge() {
        progress.setVisibility(View.GONE);
        new AlertDialog.Builder(AgentCreatePresentation.this)
                .setTitle("Insufficient Balance")
                .setMessage("Recharge Your Wallet to Buy PDF")
                .setPositiveButton("Recharge", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent it = new Intent(getApplicationContext(), RechargeWallet.class);
                        startActivity(it);
                    }
                })
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }


    //Checking Available Wallet Balance Of User
    //if user do not have available balance Showing Alert Dialogue to recharge balance

    public void getAmountFromDatabase() {

        progress.setVisibility(View.VISIBLE);

        final String[] walletPrevAmount = {"0"};
        DatabaseReference getPrevAmount = mRoot.child("users").child("customer")
                .child("registered").child("detail").child(mAuth.getUid());
        getPrevAmount.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("wallet")) {
                    progress.setVisibility(View.GONE);
                    walletPrevAmount[0] = dataSnapshot.child("wallet").getValue().toString();
                    if (Integer.parseInt(walletPrevAmount[0]) >= PdfAmount) {

                        deductWallet(PdfAmount);
                    } else
                        showAlertTorecharge();

                } else {
                    walletPrevAmount[0] = "0";
                    showAlertTorecharge();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    //deducting wallet amount
    public void deductWallet(final int amount) {
        final DatabaseReference deductamountDB = mRoot.child("users").child("customer").child("registered").child("detail")
                .child(mAuth.getUid());
        deductamountDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("wallet")) {
                    int prevAmount = Integer.parseInt(dataSnapshot.child("wallet").getValue().toString());
                    deductamountDB.child("wallet").setValue(String.valueOf(prevAmount - amount));
                    NotificationReminder notificationReminder = new NotificationReminder(getApplicationContext()
                            , "Wallet",
                            "Rs " + amount + " has deducted from Your wallet");
                    try {
                        createPDF();
                    } catch (IOException | DocumentException e) {
                        e.printStackTrace();
                    }
                    progress.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(getApplicationContext(), databaseError.getMessage()).show();
            }
        });
    }

    //After Checking Balance Creating PDF and Saving it to File
    public void createPDF() throws IOException, DocumentException {
        Document document = new Document();
        File outpath = new File(Environment.
                getExternalStoragePublicDirectory(Environment.
                        DIRECTORY_DOWNLOADS), "/" + PdfName);

        try {
            PdfWriter.getInstance(document, new FileOutputStream(outpath));
            document.open();
            addTitlePage(document, string);
            document.close();
            Toasty.success(getApplicationContext(), "Success", Toasty.LENGTH_LONG).show();
            showSuccess();
            Log.d("PDF", "success");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.d("PDFEvent", "Error " + e.getMessage());
            Toasty.error(getApplicationContext(), "Error : " + e.getMessage()).show();

        }
    }


    //Adding title of PDF
    private void addTitlePage(Document document, String msg)
            throws DocumentException, IOException {

        // get input stream
        Paragraph preface = new Paragraph();
        // We add one empty line
        preface.setAlignment(Element.ALIGN_CENTER);
        // Lets write a big header
        addEmptyLine(preface, 2);
        preface.add(new Paragraph("Title of the document", catFont));
        addEmptyLine(preface, 2);

        try {
            InputStream ims = getAssets().open("covid.jpg");
            Bitmap bmp = BitmapFactory.decodeStream(ims);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.setAlignment(Element.ALIGN_CENTER);
          //  image.scaleToFit(500,200);
            image.setScaleToFitLineWhenOverflow(true);
            document.add(image);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        addEmptyLine(preface, 2);
        String[] splitedString = msg.split("--");
        int l = splitedString.length;
        int i = 0;
        preface.setAlignment(Element.ALIGN_LEFT);
        while (i < l) {
            Log.d("PDFString", l + " " + i);
            // Lets write a big header
            preface.add(new Paragraph(splitedString[i], subFont));
            addEmptyLine(preface, 1);
            i++;
        }
        document.add(preface);
        // Start a new page
        document.newPage();
    }


    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }


    //displaying pdf Storage
    public void showSuccess() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(AgentCreatePresentation.this);
            builder.setTitle("Success")
                    .setMessage("File Saved at Storage/Download/" + PdfName)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            builder.create().show();
        } catch (Exception e) {
            Toasty.error(getApplicationContext(), e.getMessage()).show();
        }
    }






    /* **********************************************************/
    /* **********************************************************/

    /*           Presentation Method

    /* **********************************************************/
    /* **********************************************************/


    public void checkUniqueDB() {
        checkUnique = mRoot.child("unique").child("presentation");
        checkUnique.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(presentationUniqueIdl)) {
                    presentationUniqueIdl = getRandomNumberString();
                    checkUniqueDB();

                } else {
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
        DatabaseReference updatePresentationstring = mRoot.child("users").child("agent").child(mAuth.getUid())
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

                        Deduct d = new Deduct(getApplicationContext());
                        d.deductPresentation(1);
                        progress.setVisibility(View.GONE);
                        Toasty.success(getApplicationContext(), "Presentation Created successfully", Toasty.LENGTH_LONG, true).show();
                        startActivity(new Intent(AgentCreatePresentation.this, AgentViewPresentation.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                });
    }

    public String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(9999);
        String sid = UUID.randomUUID().toString();

        // this will convert any number sequence into 6 character.
        return (total_present + sid.substring(2, 6) + String.format("%06d", number));
    }


    public void getTotalPresent() {
        DatabaseReference getPresentDB = mRoot.child("agent").child(mAuth.getUid())
                .child("presentation");
        getPresentDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                total_present = String.valueOf(dataSnapshot.getChildrenCount());
                // Toasty.info(getApplicationContext(),total_present).show();
                presentationUniqueIdl = getRandomNumberString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(getApplicationContext(), databaseError.getMessage()).show();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mRoot = FirebaseDatabase.getInstance().getReference();
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
