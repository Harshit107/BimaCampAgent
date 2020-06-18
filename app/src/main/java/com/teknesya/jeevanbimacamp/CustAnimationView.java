package com.teknesya.jeevanbimacamp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teknesya.jeevanbimacamp.Adapter.RecyclerAdapter;
import com.teknesya.jeevanbimacamp.model.RemoveClickListner;

import java.util.ArrayList;
import java.util.Calendar;

import co.mobiwise.materialintro.animation.MaterialIntroListener;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.view.MaterialIntroView;
import es.dmoral.toasty.Toasty;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import nl.dionsegijn.steppertouch.OnStepCallback;
import nl.dionsegijn.steppertouch.StepperTouch;
import pl.droidsonroids.gif.GifImageView;


public class CustAnimationView extends Activity implements RemoveClickListner {
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ImageView btnAddItem;
    ArrayList<RecyclerData> myList = new ArrayList<>();
    String presentationString = "Hi";
    ImageView crossImage;
    int counter = 0;
    int increase = 0;
    int lengthString = 0;
    GifImageView gifImageView;
    boolean checker = false;
    String[] mainString;
    boolean isVideoShare = false;
    DatabaseReference mRoot, checkVideoShare;
    MaterialIntroView.Builder materialIntroView;

    ImageView repearPresentation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_cust_animation_view);

        gifImageView = findViewById(R.id.tkview);
        //gifImageView.setVisibility(View.GONE);
        repearPresentation = findViewById(R.id.repeatP);
        repearPresentation.setVisibility(View.INVISIBLE);
        mRoot = FirebaseDatabase.getInstance().getReference();

        Intent it = getIntent();
        mainString = it.getStringArrayExtra("string");

        materialIntroView = new MaterialIntroView.Builder(CustAnimationView.this);

        try {
            lengthString = mainString.length;
        } catch (Exception e) {
            Log.d("CustAni",e.getMessage());
        }

        btnAddItem = (ImageView) findViewById(R.id.btnAddItem);
        showCase();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerAdapter = new RecyclerAdapter(myList, this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        final StepperTouch stepperTouch = findViewById(R.id.stepperTouch);
        stepperTouch.setMinValue(5);
        stepperTouch.setMaxValue(30);
        stepperTouch.performLongClick();
        stepperTouch.setCount(10);
        stepperTouch.setSideTapEnabled(true);
        stepperTouch.addStepCallback(new OnStepCallback() {
            @Override
            public void onStep(int value, boolean positive) {
                mRecyclerAdapter.setText(stepperTouch.getCount() * 2);
            }
        });


        repearPresentation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checker) {
                    gifImageView.setVisibility(View.GONE);
                    final LayoutAnimationController controller =
                            AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.layout_animation);
                    mRecyclerView.setLayoutAnimation(controller);
                    mRecyclerAdapter = new RecyclerAdapter(myList, this);
                    final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    mRecyclerView.setLayoutManager(layoutManager);
                    mRecyclerView.setAdapter(mRecyclerAdapter);
                    mRecyclerAdapter.setText(stepperTouch.getCount() * 2);
                    while (counter < lengthString) {
                        presentationString = mainString[counter];
                        RecyclerData mLog = new RecyclerData();
                        mLog.setTitle(presentationString);
                        myList.add(mLog);
                        mRecyclerAdapter.notifyData(myList);
                        counter++;

                    }
                    mRecyclerView.scheduleLayoutAnimation();

                }
            }
        });
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CustAnimationView.this);
                    builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setMessage("Do You want To Exit Presentation.")
                            .setCancelable(false);
                    try {
                        builder.create().show();
                    } catch (Exception e) {
                        Toasty.info(getApplicationContext(), "Exiting Presentation", Toasty.LENGTH_LONG).show();
                        finish();
                    }


                } else if (increase == 0) {
                    try {
                        gifImageView.setVisibility(View.GONE);
                        addList();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    OnRemoveClick(counter);
                    try {
                        addList();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    @Override
    public void OnRemoveClick(int index) {
        myList.remove(index);
        mRecyclerAdapter.notifyData(myList);
    }

    public void addList() throws InterruptedException {

        if (increase >= lengthString) {
            repearPresentation.setVisibility(View.VISIBLE);
            gifImageView.setImageResource(R.drawable.tk1);
            gifImageView.setBackgroundColor(getResources().getColor(R.color.black));
            showAnnim();
            btnAddItem.setImageResource(R.drawable.ic_exit_to_app_black_24dp);
            checker = true;
            return;

        }
        presentationString = mainString[increase];
        RecyclerData mLog = new RecyclerData();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.layout_animation);
        mRecyclerView.setLayoutAnimation(controller);
        mLog.setTitle(presentationString);
        myList.add(mLog);
        mRecyclerAdapter.notifyData(myList);
        mRecyclerView.scheduleLayoutAnimation();
        increase += 1;

    }


    public void showAnnim() {

        Display mdisp = getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);
        int maxX = mdispSize.x;
        int maxY = mdispSize.y;
        KonfettiView viewKonfetti = findViewById(R.id.viewKonfetti);
        viewKonfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.BLUE, getResources().getColor(R.color.colorPrimary))
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .setPosition((float) (maxX / 2), 0.0f)
                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                .addSizes(new Size(12, 5))
                .streamFor(300, 3000L);
        gifImageView.setVisibility(View.VISIBLE);

    }


    public void showCase() {
        final SharedPreferenceValue sharedPreferenceValue = new SharedPreferenceValue(getApplicationContext());

        if (sharedPreferenceValue.getPresentation().equals("0")) {
            materialIntroView
                    .enableIcon(false)
                    .setFocusGravity(FocusGravity.CENTER)
                    .setFocusType(Focus.MINIMUM)
                    .setDelayMillis(500)
                    .enableFadeAnimation(true)
                    .performClick(true)
                    .setFocusType(Focus.ALL)
                    .setTargetPadding(120)
                    .enableDotAnimation(true)
                    .setFocusGravity(FocusGravity.CENTER)
                    .setFocusType(Focus.MINIMUM)
                    .setFocusType(Focus.NORMAL)
                    .setFocusType(Focus.ALL)
                    .setInfoText("Click Here to Show Next Slide")
                    .setTextColor(getResources().getColor(R.color.colorPrimary))
                    .setTarget(btnAddItem)
                    .setListener(new MaterialIntroListener() {
                        @Override
                        public void onUserClicked(String materialIntroViewId) {
                            Toasty.success(getApplicationContext(), "Great!").show();
                            materialIntroView
                                    .enableIcon(false)
                                    .setFocusGravity(FocusGravity.CENTER)
                                    .setFocusType(Focus.MINIMUM)
                                    .setDelayMillis(1200)
                                    .enableFadeAnimation(true)
                                    .performClick(true)
                                    .setFocusType(Focus.ALL)
                                    .setTargetPadding(120)
                                    .enableDotAnimation(true)
                                    .setFocusGravity(FocusGravity.CENTER)
                                    .setFocusType(Focus.MINIMUM)
                                    .setFocusType(Focus.NORMAL)
                                    .setFocusType(Focus.ALL)
                                    .setInfoText("Click Again to  Show Next Slide")
                                    .setTextColor(getResources().getColor(R.color.colorPrimary))
                                    .setTarget(btnAddItem)
                                    .setListener(new MaterialIntroListener() {
                                        @Override
                                        public void onUserClicked(String materialIntroViewId) {
                                            Toasty.success(getApplicationContext(), "Great!").show();
                                            sharedPreferenceValue.updatePresentation("1");
                                        }
                                    })
                                    .setUsageId(String.valueOf(Calendar.getInstance().getTimeInMillis())) //THIS SHOULD BE UNIQUE ID
                                    .show();

                        }
                    })
                    .setUsageId(String.valueOf(Calendar.getInstance().getTimeInMillis())) //THIS SHOULD BE UNIQUE ID
                    .show();


        }
    }


}