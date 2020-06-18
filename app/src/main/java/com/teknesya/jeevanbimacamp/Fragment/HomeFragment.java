package com.teknesya.jeevanbimacamp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;
import com.teknesya.jeevanbimacamp.Adapter.SliderAdapterExample;
import com.teknesya.jeevanbimacamp.AgentCreatePresentation;
import com.teknesya.jeevanbimacamp.AgentViewPresentation;
import com.teknesya.jeevanbimacamp.R;
import com.teknesya.jeevanbimacamp.UserForm;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class HomeFragment extends Fragment {

    Context context;
    TextView setting_name,setting_email,setting_plan;
    DatabaseReference update_info,mRoot,customerCountDB;
    View progress;
    TextView ptext;
    CircleImageView profile_pic;
    FirebaseAuth mAuth;

    FrameLayout fAvailablePolicy,fAddCustomer,fViewCustomer,fCreatePresentation,fViewPresentation;
    TextView customerCount;


    LinearLayout linearLayoutManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        context=container.getContext();
        return inflater.inflate(R.layout.homepagefragment, container, false);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView textview = (TextView)getActivity().findViewById(R.id.tv_name);
        textview.setText(getResources().getString(R.string.app_name));
        initilize();

        SliderView sliderView =view. findViewById(R.id.imageSlider);
        SliderAdapterExample adapter = new SliderAdapterExample(getContext());
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimations.DROP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(getResources().getColor(R.color.colorPrimary));
        sliderView.getAutoCycleDirection();
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        fAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it=new Intent(getContext(), UserForm.class);
                startActivity(it);

            }
        });
        fViewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Fragment f=new AgentCustomerSearchFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.framelayout, f);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        fCreatePresentation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AgentCreatePresentation.class));
               getActivity(). overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
        fViewPresentation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AgentViewPresentation.class));
                getActivity(). overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

    }

    public void initilize()
    {
        progress=getActivity().findViewById(R.id.progress_bar);
        ptext=progress.findViewById(R.id.progress_text);
        progress.setVisibility(View.VISIBLE);
        ptext.setText("Please wait");
        mAuth= FirebaseAuth.getInstance();
        mRoot= FirebaseDatabase.getInstance().getReference();
        TextView textview = (TextView)getActivity().findViewById(R.id.tv_name);
        textview.setText(getResources().getString(R.string.app_name));


        profile_pic=getActivity().findViewById(R.id.profile_pic);
        setting_name=getActivity().findViewById(R.id.name);
        setting_email=getActivity().findViewById(R.id.email);
        setting_plan=getActivity().findViewById(R.id.plan);

        fAvailablePolicy=getActivity().findViewById(R.id.fAvailablePolicy);
        fAddCustomer=getActivity().findViewById(R.id.fAddCustomer);
        fViewCustomer=getActivity().findViewById(R.id.fViewCustomer);
        fCreatePresentation=getActivity().findViewById(R.id.fCreatePresentation);
        fViewPresentation=getActivity().findViewById(R.id.fViewPresentation);
        customerCount=getActivity().findViewById(R.id.addCustomerCount);


        customerCountDB=mRoot.child("users").child("agent").child(mAuth.getUid()).child("customer");
        customerCountDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerCount.setText("Total Customer Found : "+String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        update_info=mRoot.child("users").child("customer").child("registered").child("detail").child(mAuth.getUid());
        update_info.keepSynced(true);
        update_info.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("name"))
                    setting_name.setText(dataSnapshot.child("name").getValue().toString());
                if(dataSnapshot.hasChild("lastlogin"))
                    setting_plan.setText("Last Login : "+dataSnapshot.child("lastlogin").getValue().toString());
                if(dataSnapshot.hasChild("image"))
                    Picasso.get().load(dataSnapshot.child("image").getValue().toString()).placeholder(R.drawable.avtar).into(profile_pic);
                if(dataSnapshot.hasChild("email"))
                    setting_email.setText(dataSnapshot.child("email").getValue().toString());
                else
                    Toasty.warning(getContext(),"Warning : Update Your Email ",Toasty.LENGTH_LONG,true).show();
                progress.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(getContext(),databaseError.getMessage(),Toasty.LENGTH_LONG,true).show();

            }
        });
    }



}
