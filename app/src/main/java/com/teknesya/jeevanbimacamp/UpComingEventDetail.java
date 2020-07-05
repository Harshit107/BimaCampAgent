package com.teknesya.jeevanbimacamp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teknesya.jeevanbimacamp.Adapter.CustomerRecyclerAdapter;
import com.teknesya.jeevanbimacamp.Adapter.CustomerUpCommingRecyclerAdapter;
import com.teknesya.jeevanbimacamp.Utils.DateBima;
import com.teknesya.jeevanbimacamp.model.CustomerlListings;

import java.io.DataInput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;


public class UpComingEventDetail extends AppCompatActivity  {

    TextView selectedEvent_tv, dateSelected;
    RecyclerView eventRecyclerView;
    ArrayList<CustomerlListings> eventArrayList = new ArrayList<>();
    FirebaseAuth mAuth;
    DatabaseReference mRoot, getDataBase, customerDetailDB;
    String typeEvent="";
    int increasedDate = 5;
    int databaseYear;

    public static String typeEventDetail = "lead";
    CustomerUpCommingRecyclerAdapter eventRecyclearAdapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_comming_event_detail);



        mRoot = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        selectedEvent_tv = findViewById(R.id.upcomingEventName);
        dateSelected = findViewById(R.id.date);
        databaseYear=DateBima.getYear();

        //recycler


        //
        Intent event = getIntent();
        typeEvent = event.getStringExtra("type");
        typeEventDetail=typeEvent;
        //
        try {
            initDatabase(typeEvent);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        ImageView back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        dateSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] date = {
                        "Next 5 days",
                        "Next 1 Months",
                        "Next 3 Months",
                        "Next 5 Months",
                        "Next 8 Months"
                };
                final String[] selectedItem = {"5 D"};
                int checkList = 0;
                AlertDialog.Builder showDate = new AlertDialog.Builder(UpComingEventDetail.this);
                showDate.setSingleChoiceItems(date, checkList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                increasedDate = 5;
                                selectedItem[0] = "5 D";
                                break;
                            case 1:
                                increasedDate = 30;
                                selectedItem[0] = "1 M";
                                break;
                            case 2:
                                increasedDate = 90;
                                selectedItem[0] = "3 M";
                                break;
                            case 3:
                                increasedDate = 150;
                                selectedItem[0] = "5 M";
                                break;
                            case 4:
                                increasedDate = 240;
                                selectedItem[0] = "8 M";
                                break;
                            default:
                                increasedDate = 5;
                                selectedItem[0] = "5 D";
                        }
                        dateSelected.setText(selectedItem[0]);

                    }
                });
                showDate.setTitle("Select Date");
                showDate.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dateSelected.setText(selectedItem[0]);
                        try {
                            initDatabase(typeEvent);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                });
                showDate.create().show();


            }
        });

    }






    /*      -----------------------
            set recyclerView
     */
    public void recyclerView()
    {
        eventRecyclerView = findViewById(R.id.eventRecycler);
        eventRecyclearAdapter = new CustomerUpCommingRecyclerAdapter(eventArrayList,
                getApplicationContext(), UpComingEventDetail.this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        eventRecyclerView.setLayoutManager(layoutManager);
        eventRecyclerView.setAdapter(eventRecyclearAdapter);
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initDatabase(String typeEvent) throws ParseException {
        recyclerView();
        typeEventDetail=typeEvent;
        customerDetailDB = mRoot.child("users").child("agent").child(mAuth.getUid()).child("customer");
        if (typeEvent.equals("premium")) {

            selectedEvent_tv.setText("UpComing Premium");
            getDataBase = mRoot.child("users").child("agent").child(mAuth.getUid()).child("premium");
            getPremiumDetail(increasedDate);
        } else if (typeEvent.equals("birthday")) {

            selectedEvent_tv.setText("UpComing Birthday");
            getDataBase = mRoot.child("users").child("agent").child(mAuth.getUid()).child("date");
            loadData(increasedDate);
        }  else if (typeEvent.equals("lead")) {

            selectedEvent_tv.setText("UpComing Lead");
            getDataBase = mRoot.child("users").child("agent").child(mAuth.getUid()).child("date");
            loadData(increasedDate);
        } else {
            selectedEvent_tv.setText("UpComing Anniversary");
            getDataBase = mRoot.child("users").child("agent").child(mAuth.getUid()).child("date");
            loadData(increasedDate);
        }



    }



        /*----------------------------------------
        *************************** Premium ***************************
        *-----------------------------------------
        *  */

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getPremiumDetail(int days) throws ParseException {

        eventArrayList = new ArrayList<>();
        eventArrayList.clear();
        //today Date

        //today date+month
        String todayComareDate = DateBima.getDateInStringNumFormate();
        final Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(todayComareDate);

        final Calendar upComingCalendar = Calendar.getInstance();
        upComingCalendar.add(Calendar.DATE, days);
        final Date upcomingDate = upComingCalendar.getTime();



        getDataBase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {

                    //key Date from database
                    final String databaseDate = dataSnapshot.getKey();
                   // Log.d("dateEvent","date --"+String.valueOf(databaseYear));
                    Date databaseConvertDate = date1;
                    try {
                        databaseConvertDate = new SimpleDateFormat("dd-MM-yyyy").parse(databaseDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //Checking Condition of date

                    Log.d("dateEvent","dabase date"+String.valueOf(databaseConvertDate));
                    Log.d("dateEvent","current date"+String.valueOf(date1));
                    Log.d("dateEvent","additional date"+String.valueOf(upcomingDate));
                    Log.d("dateEvent","***");

                    if(databaseConvertDate.after(date1) && databaseConvertDate.before(upcomingDate)){
                        Log.d("dateEvent","Date1 is after Date2");
                    }
                    else
                        Log.d("dateEvent","no");

                    if (databaseConvertDate.after(date1) && databaseConvertDate.before(upcomingDate)) {

                            getDataBase.child(databaseDate).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                    final String customerKey = dataSnapshot.getValue().toString();
                                    Log.d("event", customerKey);

                                    customerDetailDB.child(customerKey).child("detail").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Log.d("event", dataSnapshot.child("name").getValue().toString());
                                            String name = dataSnapshot.child("name").getValue().toString();
                                            String email = dataSnapshot.child("email").getValue().toString();
                                            String plan = dataSnapshot.child("plan").getValue().toString();
                                            String phone = dataSnapshot.child("phone").getValue().toString();
                                            String image = "";
                                            try {
                                                image = dataSnapshot.child("image").getValue().toString();
                                            } catch (Exception e) {
                                                image = "Default";
                                            }

                                            CustomerlListings javamesssage = new CustomerlListings(name, image, plan, email, customerKey, databaseDate, phone);
                                            eventArrayList.add(javamesssage);
                                            eventRecyclearAdapter.notifyDataSetChanged();
                                            setAdapter();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Toasty.error(getApplicationContext(), databaseError.getMessage()).show();
                                        }
                                    });


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
                }

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
                Toasty.error(getApplicationContext(),databaseError.getMessage()).show();

            }
        });

    }





































    /****       *******************   for Birthday and Anniversary ***********      *****/


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void loadData(int days) throws ParseException {


        eventArrayList = new ArrayList<>();
        eventArrayList.clear();
        //today Date

        //current year
        databaseYear=DateBima.getYear();

        //today date+month
        String todayComareDate = DateBima.getDateInStringNumFormate();
        final Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(todayComareDate);

        final Calendar upComingCalendar = Calendar.getInstance();
        upComingCalendar.add(Calendar.DATE, days);
        final Date upcomingDate = upComingCalendar.getTime();


        getDataBase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {

                    //key Date from database
                    final String databaseDate = dataSnapshot.getKey();


                    //extracting month feom database date
                    int monthOfDatataBaseDate = DateBima.convertDateMonthToMonth(databaseDate);



                    //increasing year if month is lesser than todays month
                    if(monthOfDatataBaseDate<DateBima.getMonth()){
                        databaseYear++;
                        Log.d("dateEvent","Month database"+monthOfDatataBaseDate);
                        Log.d("dateEvent","Month current"+DateBima.getMonth());

                    }
                    else
                    {
                        databaseYear=DateBima.getYear();
                    }

                    Date databaseConvertDate = date1;
                    try {
                        databaseConvertDate = new SimpleDateFormat("dd-MM-yyyy").parse(databaseDate+"-"+databaseYear);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //Checking Condition of date

                    Log.d("dateEvent","dabase date"+String.valueOf(databaseConvertDate));
                    Log.d("dateEvent","current date"+String.valueOf(date1));
                    Log.d("dateEvent","additional date"+String.valueOf(upcomingDate));
                    Log.d("dateEvent","***");

                    if(databaseConvertDate.after(date1) && databaseConvertDate.before(upcomingDate)){
                        Log.d("dateEvent","Date1 is after Date2");
                    }
                    else
                        Log.d("dateEvent","no");





                    if (databaseConvertDate.after(date1) && databaseConvertDate.before(upcomingDate)) {

                        if (dataSnapshot.hasChild(typeEvent)) {
                            getDataBase.child(databaseDate).child(typeEvent).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                    final String customerKey = dataSnapshot.getKey();



                                    //for lead
                                    if(typeEvent.equals("lead")){

                                       DatabaseReference groupnameref = FirebaseDatabase.getInstance().getReference()
                                                .child("users").child("agent").child(mAuth.getUid()).child("lead").child(customerKey);
                                        groupnameref.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                //Log.d("event", dataSnapshot.child("name").getValue().toString());
                                                String name = dataSnapshot.child("Name").getValue().toString();
                                                String email = "Email Not Found";
                                                try {
                                                    email = dataSnapshot.child("email").getValue().toString();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                //String plan = dataSnapshot.child("plan").getValue().toString();
                                                String phone = dataSnapshot.child("Phone").getValue().toString();
                                                String image = "";
                                                try {
                                                    image = dataSnapshot.child("image").getValue().toString();
                                                } catch (Exception e) {
                                                    image = "Default";
                                                }

                                                CustomerlListings javamesssage = new CustomerlListings(name, image, "No Plan Found", email, customerKey, databaseDate, phone);
                                                eventArrayList.add(javamesssage);
                                                eventRecyclearAdapter.notifyDataSetChanged();
                                                setAdapter();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Toasty.error(getApplicationContext(), databaseError.getMessage()).show();
                                            }
                                        });

                                    }

                                    //for other
                                    else {

                                        Log.d("event", customerKey);

                                        customerDetailDB.child(customerKey).child("detail").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                Log.d("event", dataSnapshot.child("name").getValue().toString());
                                                String name = dataSnapshot.child("name").getValue().toString();
                                                String email = dataSnapshot.child("email").getValue().toString();
                                                String plan = dataSnapshot.child("plan").getValue().toString();
                                                String phone = dataSnapshot.child("phone").getValue().toString();
                                                String image = "";
                                                try {
                                                    image = dataSnapshot.child("image").getValue().toString();
                                                } catch (Exception e) {
                                                    image = "Default";
                                                }

                                                CustomerlListings javamesssage = new CustomerlListings(name, image, plan, email, customerKey, databaseDate, phone);
                                                eventArrayList.add(javamesssage);
                                                eventRecyclearAdapter.notifyDataSetChanged();
                                                setAdapter();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Toasty.error(getApplicationContext(), databaseError.getMessage()).show();
                                            }
                                        });
                                    }

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


                        } else
                            Log.d("event", "not found");


                    }
                }

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
                Toasty.error(getApplicationContext(),databaseError.getMessage()).show();

            }
        });


    }
    private void setAdapter() {
        eventRecyclearAdapter = new CustomerUpCommingRecyclerAdapter(eventArrayList, getApplicationContext(),this);
        eventRecyclerView.setHasFixedSize(true);
        eventRecyclerView.setAdapter(eventRecyclearAdapter);
    }

    /*
    * ************************      Complete Database and Anniversary       ************************/


}
